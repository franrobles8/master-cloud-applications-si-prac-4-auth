const config = require("../config");
const jwt = require("jsonwebtoken");
const { User } = require("../models/user");

const verifyUser = (req, res, next) => {
    const token = extractToken(req);

    if (!token) return res.status(401).send({auth: false, message: "No token provided. "});

    jwt.verify(token, config.JWT_SECRET, (jwtErr, decoded) => {
        if (jwtErr) return res.status(500).send({auth: false, message: "Failed to authenticate token."});

        User.findById(decoded.id, { password: 0 }, (dbError, user) => {
            if (dbError) return res.status(500).send({auth: false, message: "Failed to find user associated to the token."});
            if (!user) return res.status(401).send({auth: false, message: "No user found for that token."});
            next();
        });
    });
};

const extractToken = (req) => {
    if (req.headers.authorization && req.headers.authorization.split(' ')[0] === 'Bearer') {
        return req.headers.authorization.split(' ')[1];
    }
    return null;
}

module.exports = {
    verifyUser
};