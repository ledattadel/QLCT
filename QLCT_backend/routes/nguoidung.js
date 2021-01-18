const express = require("express");
const router = express.Router();
const bodyParser = require("body-parser");
const multer = require("multer");
const upload = multer();
const bcrypt = require('bcrypt');

const Sequelize = require("sequelize");
const sequelize = require("../database/database");
const Nguoidung = require("../models/nguoidung");
const Thongtin_nguoidung = require("../models/thongtin_nguoidung");
const Taikhoan = require("../models/taikhoan");

const jsonParser = bodyParser.json();
router.use(bodyParser.urlencoded({ extended: true }));

const urlencodedParser = bodyParser.urlencoded({ extended: false });

router.use(upload.array());

//Insert nguoidung, thongtin_nguoidung, taichinh
router.post("/dangky", urlencodedParser, async (req, res) => {
  Nguoidung.findOne({ where: { tennguoidung: req.body.tennguoidung } })
    .then((nguoidung) => {
      if (nguoidung != null) {
        return res.status(409).json({
          message: "Email đã tồn tại",
        });
      } else {
        bcrypt.hash(req.body.matkhau, 10, (err, hash) => {
          if (err) {
            console.log(err);
              return res.status(500).json({
                  error: err
              })
          } else {
            sequelize
              .query(
                `CALL sp_insert_nd('${req.body.tennguoidung}', '${hash}', '${req.body.hoten}', '${req.body.sodienthoai}');`
              )
              .then((data) => {
                res.status(201).json({
                  message: "Thêm người dùng thành công",
                });
              })
              .catch((err) => {
                console.log(err);
                res.status(500).json({
                  message: "Thêm người dùng thất bại",
                });
              });
          }
      })
      }
    })
    .catch((err) => {
      console.log(err);
      res.status(500).json({
        error: err,
      });
    });
});
//Đăng nhập người dùng
router.post("/dangnhap", urlencodedParser, async (req, res) => {
  Nguoidung.findOne({where:{tennguoidung: req.body.tennguoidung}})
  .then(nguoidung => {
    if(nguoidung){
      bcrypt.compare(req.body.matkhau, nguoidung.matkhau, (err, result) => {
        if (err || !result) {
            res.status(401).json({
                message: 'Mật khẩu không chính xác'
            });
        }else{
          Thongtin_nguoidung.findOne({where: {ma_nguoidung : nguoidung.ma_nguoidung}})
          .then(thongtin_nguoidung =>{
            console.log(thongtin_nguoidung.ma_taikhoan);
            return res.status(200).json({
                ma_nguoidung : nguoidung.ma_nguoidung,
                ma_taikhoan:  thongtin_nguoidung.ma_taikhoan,
                message: 'Đăng nhập thành công'
            });
            })
          .catch(err =>{
            res.status(200).json({
              message : err
            });
          })
        }
    })
    }
    else{
      res.status(401).json({
                message: 'Email không tồn tại'
            });
    }
  })
  .catch(err => {
    console.log(err);
        res.status(500).json({
            error: err
        });
    });
})
//GET all nguoidung
router.get("/", async (req, res) => {
  try {
    const nguoidung = await Nguoidung.findAll({
      attributes: ["tennguoidung", "matkhau", "ma_nguoidung"],
    });
    res.status(200).json({
      nguoidung : nguoidung
      });
  } catch (error) {
    res.status(500).json({
      message: `failed. Error: ${error}`,
    });
  }
});
//GET one
router.get("/:tennguoidung", async (req, res)=>{
  Nguoidung.findOne({where: {tennguoidung: req.params.tennguoidung}})
  .then(nguoidung=>{
    if(nguoidung){
      res.status(200).json({
        nguoidung : nguoidung
      });
    }
    else{
      res.status(404).json({
        message: "Người dùng không tồn tại"
      });
    }
  })
  .catch(err=>{
    res.status(500).json({
      error: err
    });
  });
})
//Update mật khẩu
router.patch("/:ma_nguoidung", urlencodedParser, async (req, res) => {

  Nguoidung.findOne({where: {ma_nguoidung: req.params.ma_nguoidung}})
  .then(nguoidung=>{
    if(nguoidung){
      bcrypt.compare(req.body.matkhau, nguoidung.matkhau, (err, result) => {
        if (err || !result) {
            res.status(401).json({
                message: 'Mật khẩu cũ không chính xác'
            });
        }

        else{
          bcrypt.hash(req.body.matkhaumoi, 10, (err, hash) => {
          if (err) {
            console.log(err);
              return res.status(500).json({
                  error: err
              })
          } else {
            Nguoidung.update({matkhau: hash} ,{where:{ma_nguoidung: req.params.ma_nguoidung}})
            .then(nguoidung=>{
              return res.status(200).json({
                message: "Cập nhật mật khẩu thành công"
              });
            })
            .catch(err=>{
              return res.status(500).json({
                message: "Cập nhật mật khẩu thất bại"
              });
            })
          }
          })
        }
      })
    }
    else{
      return res.status(404).json({
              message: "Người dùng không tồn tại"
            });
    }

  })
})

//update thongtin_nguoidung
router.patch("/thongtin_nguoidung/:id", urlencodedParser, async (req, res) => {
  const id = req.params.id;
  const updateOps = {};
  const alowEditProps = [
    "hoten",
    "gioitinh",
    "sodienthoai",
    "diachi",
    "nghenghiep",
  ];
  for (const ops of req.body) {
    if (alowEditProps.includes(ops.prop)) {
      updateOps[ops.prop] = ops.value;
    }
  }
  try {
    let result = await Thongtin_nguoidung.update(updateOps, {
      where: { ma_nguoidung: id },
    });
    await res.status(200).json({
      message: "Cập nhật thông tin thành công",
    });
  } catch (error) {
    res.status(500).json({
      message: "Cập nhật thông tin thất bại",
    });
  }
});
//GET thongtin_nguoidung
router.get("/thongtin/:manguoidung", async (req, res)=>{
  Thongtin_nguoidung.findOne({where: {ma_nguoidung: req.params.manguoidung}})
  .then(thongtin_nguoidung=>{
    if(thongtin_nguoidung){
      res.status(200).json(thongtin_nguoidung);
    }
    else{
      res.status(404).json({
        message: "thông tin người dùng không tồn tại"
      });
    }
  })
  .catch(err=>{
    res.status(500).json({
      error: err
    });
  });
})
module.exports = router;
