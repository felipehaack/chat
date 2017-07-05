const path = require('path');
const express = require('express');
const app = express();

app.use(express.static(__dirname + '/dist'))

app.get('/*', function (req, res) {
  res.sendFile(path.join(__dirname + '/dist/index.html'))
})

const PORT = 4200
const HOST = "0.0.0.0"

app.listen(PORT, HOST)

console.log("server is running! http://" + HOST + ":" + PORT)