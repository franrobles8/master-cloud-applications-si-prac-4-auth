const express = require('express');
const fs = require('fs');
const https = require('https');
const database = require('./database.js');
const booksRouter = require('./routes/bookRouter.js');
const usersRouter = require('./routes/userRouter.js');
const authRouter = require('./routes/authRouter.js');
const app = express();
const PORT = process.env.PORT || 3443;

//Convert json bodies to JavaScript object
app.use(express.json());
app.use('/api/v1/books', booksRouter);
app.use('/api/v1/users', usersRouter);
app.use('/api/v1', authRouter);

async function main() {

    await database.connect();

    https.createServer({
        key: fs.readFileSync('./server.key'),
        cert: fs.readFileSync('./server.cert'),
    }, app).listen(PORT, () => {
        console.log(`Server listening on ${PORT} !`);
    });

    process.on('SIGINT', () => {
        database.disconnect();
        console.log('Process terminated');
        process.exit(0);
    });
}

main();