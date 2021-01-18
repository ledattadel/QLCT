const express = require("express");
const router = express.Router();
const bodyParser = require("body-parser");
const multer = require("multer");
const upload = multer();
const bcrypt = require('bcrypt');
const currencyFormatter = require('currency-formatter');

const Sequelize = require("sequelize");
const sequelize = require("../database/database");
const Taikhoan = require("../models/taikhoan");

//get sodu
router.get("/sodu/:id", async (req, res) => {
    const id = req.params.id;
  try {
    const taikhoan = await Taikhoan.findOne({
        attributes: [
          "sodu"
        ],
      where: { ma_taikhoan: id} }
    );
    const x = currencyFormatter.format(taikhoan.sodu, { locale: 'de-DE', code: "VND" });
    res.status(200).json({sodu: x});
  } catch (error) {
    res.status(500).json({
      message: `failed. Error: ${error}`,
    });
  }
});
router.get("/sodulay/:id", async (req, res) => {
    const id = req.params.id;
  try {
    const taikhoan = await Taikhoan.findOne({
        attributes: [
          "sodu"
        ],
      where: { ma_taikhoan: id} }
    );
    res.status(200).json({sodu: taikhoan.sodu});
  } catch (error) {
    res.status(500).json({
      message: `failed. Error: ${error}`,
    });
  }
});
module.exports = router;