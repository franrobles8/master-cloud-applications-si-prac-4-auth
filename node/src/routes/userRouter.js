const express = require("express");
const router = express.Router();
const { User, toResponse, isValidEmail } = require("../models/user.js");
const Book = require("../models/book.js").Book;
const mongoose = require("mongoose");
const jwt = require("jsonwebtoken");
const bcrypt = require("bcryptjs");
const config = require("../config");
const { verifyUser } = require("../middlewares/auth");

const INVALID_USER_ID_RESPONSE = { error: "Invalid user id" };
const USER_NOT_FOUND_RESPONSE = { error: "User not found" };

router.get("/", verifyUser, async (req, res) => {
  const allUsers = await User.find().exec();
  res.json(toResponse(allUsers));
});

router.get("/:id", verifyUser, async (req, res) => {
  const id = req.params.id;

  if (!mongoose.Types.ObjectId.isValid(id)) {
    return res.status(400).send(INVALID_USER_ID_RESPONSE);
  }

  const user = await User.findById(id);
  if (!user) {
    return res.status(404).send(USER_NOT_FOUND_RESPONSE);
  }

  res.json(toResponse(user));
});

router.post("/", async (req, res) => {
  const result = await User.find({ nick: req.body.nick }).exec();
  if (result.length > 0) {
    return res
      .status(409)
      .send({ error: "Already exists a user with that nick" });
  }

  const user = new User({
    nick: req.body.nick,
    email: req.body.email,
    password: bcrypt.hashSync(req.body.password, 8),
  });

  try {
    const savedUser = await user.save();
    const token = jwt.sign({ id: savedUser._id }, config.JWT_SECRET, {
      expiresIn: 86400,
    });

    res.json({
      ...toResponse(savedUser),
      token,
    });
  } catch (error) {
    console.log(error);
    res.status(400).send(error);
  }
});

router.post("/auth", async (req, res) => {
  try {
    const user = await User.findOne({
      nick: req.body.nick,
    }).exec();

    console.log(user);

    if (!user) return res.status(404).send({ error: "User not found" });

    const match = await bcrypt.compare(req.body.password, user.password);

    if (!match) return res.status(401).send({ error: "Invalid credentials" });

    const token = jwt.sign({ id: user._id }, config.JWT_SECRET, {
      expiresIn: 86400,
    });

    res.json({
      token,
    });
  } catch (error) {
    console.log(error);
    res.status(400).send(error);
  }
});

router.patch("/:id", verifyUser, async (req, res) => {
  const id = req.params.id;

  if (!mongoose.Types.ObjectId.isValid(id)) {
    return res.status(400).send(INVALID_USER_ID_RESPONSE);
  }

  if (!isValidEmail(req.body.email)) {
    return res.status(400).send({ error: "Invalid email" });
  }

  const user = await User.findById(id);
  if (!user) {
    return res.status(404).send(USER_NOT_FOUND_RESPONSE);
  }

  user.email = req.body.email;
  const updatedUser = await user.save();
  res.json(toResponse(updatedUser));
});

router.delete("/:id", verifyUser, async (req, res) => {
  const id = req.params.id;

  if (!mongoose.Types.ObjectId.isValid(id)) {
    return res.status(400).send(INVALID_USER_ID_RESPONSE);
  }

  const user = await User.findById(id);
  if (!user) {
    return res.status(404).send(USER_NOT_FOUND_RESPONSE);
  }

  const result = await Book.find({
    comments: { $elemMatch: { user: id } },
  }).exec();
  if (result.length > 0) {
    return res
      .status(409)
      .send({ error: "Can't delete user because has associated comments" });
  }

  await User.findByIdAndDelete(id);

  res.json(toResponse(user));
});

router.get("/:id/comments", verifyUser, async (req, res) => {
  const id = req.params.id;

  if (!mongoose.Types.ObjectId.isValid(id)) {
    return res.status(400).send(INVALID_USER_ID_RESPONSE);
  }

  const user = await User.findById(id);
  if (!user) {
    return res.status(404).send(USER_NOT_FOUND_RESPONSE);
  }

  const userComments = await Book.aggregate([
    { $unwind: "$comments" },
    { $match: { "comments.user": user._id } },
    {
      $project: {
        _id: 0,
        id: "$comments._id",
        bookId: "$_id",
        comment: "$comments.comment",
        score: "$comments.score",
      },
    },
  ]);

  res.json(userComments);
});

module.exports = router;
