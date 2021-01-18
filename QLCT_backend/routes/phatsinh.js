const express = require("express");
const router = express.Router();
const bodyParser = require("body-parser");
const multer = require("multer");
const upload = multer();
var moment = require("moment");
const Op = require("Sequelize").Op;
const pluck = require("arr-pluck");

const Sequelize = require("sequelize");
const sequelize = require("../database/database");
const Taikhoan = require("../models/taikhoan");
const Phatsinh = require("../models/phatsinh");
const Vayno = require("../models/vayno");

const jsonParser = bodyParser.json();
router.use(bodyParser.urlencoded({ extended: true }));

const urlencodedParser = bodyParser.urlencoded({ extended: false });

router.use(upload.array());
//Insert thu,chi
router.post("/thuchi/", urlencodedParser, async (req, res) => {
  const { ngay, noidung, loaiphatsinh, sotien, mota, ma_taikhoan } = req.body;
  try {
    let phatsinh = await Phatsinh.create(
      {
        ngay,
        noidung,
        loaiphatsinh,
        sotien,
        mota,
        ma_taikhoan,
      },
      {
        fields: [
          "ngay",
          "noidung",
          "loaiphatsinh",
          "sotien",
          "mota",
          "ma_taikhoan",
        ],
      }
    );
    if (phatsinh) {
      res.status(201).json({ phatsinh });
    } else {
      res.status(500).json({ message: "Thêm khoản thu chi thất bại" });
    }
  } catch (error) {
    res
      .status(500)
      .json({ message: `Thêm khoản thu chi thất bại. Error: ${error}` });
  }
});
//Insert vayno
router.post("/vayno/", urlencodedParser, async (req, res) => {
  const {
    ngay,
    noidung,
    loaiphatsinh,
    sotien,
    mota,
    ma_taikhoan,
    ngaytra,
    hoten_vayno,
    diachi_vayno,
    sdt_vayno,
  } = req.body;
  //   console.log(`${ngay}\n${noidung}\n${loaiphatsinh}\n${sotien}\n${mota}\n${ma_taikhoan}`);

  try {
    let phatsinh = await sequelize.query(
      `CALL sp_insert_vayno('${ngay}', 
                                  '${noidung}', 
                                  '${loaiphatsinh}', 
                                  ${sotien}, 
                                  '${mota}', 
                                  ${ma_taikhoan}, 
                                  '${ngaytra}',
                                  '${hoten_vayno}', 
                                  '${diachi_vayno}', 
                                  '${sdt_vayno}');`
    );
    // const result = await sequelize.transaction(async (t) => {
    //   let phatsinh = await Phatsinh.create(
    //     {
    //       ngay,
    //       noidung,
    //       loaiphatsinh,
    //       sotien,
    //       mota,
    //       ma_taikhoan,
    //     },
    //     {
    //       fields: [
    //         "ngay",
    //         "noidung",
    //         "loaiphatsinh",
    //         "sotien",
    //         "mota",
    //         "ma_taikhoan",
    //       ],
    //     },
    //     { transaction: t }
    //   );
    //   let ma_phatsinh = phatsinh.ma_phatsinh;
    //   await Vayno.create(
    //     {
    //       ngaytra,
    //       hoten_vayno,
    //       diachi_vayno,
    //       sdt_vayno,
    //       ma_phatsinh,
    //     },
    //     {
    //       fields: [
    //         "ngaytra",
    //         "datra",
    //         "hoten_vayno",
    //         "diachi_vayno",
    //         "sdt_vayno",
    //         "ma_phatsinh",
    //       ],
    //     },
    //     { transaction: t }
    //   );
    return res.status(201).json({
      message: "Thêm khoản vay nợ thành công",
    });
    //  });
  } catch (error) {
    res.status(500).json({
      message: `Thêm khoản vay nợ thất bại. Error: ${error}`,
    });
  }
});
//insert trả nợ và thu nợ // thu nợ :"l", trả nợ "d"
router.post("/thutra/", urlencodedParser, async (req, res) => {
  const {
    ngay,
    noidung,
    loaiphatsinh,
    sotien,
    mota,
    ma_taikhoan,
    ma_ps,
  } = req.body;
  //   console.log(`${ngay}\n${noidung}\n${loaiphatsinh}\n${sotien}\n${mota}\n${ma_taikhoan}`);
  try {
    let phatsinh = await Phatsinh.create(
      {
        ngay,
        noidung,
        loaiphatsinh,
        sotien,
        mota,
        ma_taikhoan,
      },
      {
        fields: [
          "ngay",
          "noidung",
          "loaiphatsinh",
          "sotien",
          "mota",
          "ma_taikhoan",
        ],
      }
    );
    if (phatsinh) {
      let vayno = await Vayno.update(
        {
          datra: phatsinh.ma_phatsinh,
        },
        { where: { ma_phatsinh: ma_ps } }
      );

      if (loaiphatsinh === "l") {
        res.status(201).json({
          message: "Thêm khoản thu nợ thành công",
        });
      } else {
        res.status(201).json({
          message: "Thêm khoản trả nợ thành công",
        });
      }
    } else {
      res.status(500).json({
        message: "Thêm khoản vay nợ thất bại",
      });
    }
  } catch (error) {
    res.status(500).json({
      message: `Thêm khoản vay nợ thất bại. Error: ${error}`,
    });
  }
});
//delete phatsinh
router.delete("/xoa/:id", async (req, res) => {
  let { id } = req.params;
  let phatsinh = await Phatsinh.findOne({ where: { ma_phatsinh: id } });
  if (phatsinh) {
    if (phatsinh.loaiphatsinh === "n" || phatsinh.loaiphatsinh === "v") {
      //  console.log(phatsinh.loaiphatsinh);
      let vayno = await Vayno.findOne({ where: { ma_phatsinh: id } });
      if (vayno.datra != null) {
        let a = await Phatsinh.destroy({ where: { ma_phatsinh: vayno.datra } });
      }
      Vayno.destroy({
        where: { ma_phatsinh: id },
      })

        .then((phatsinh) => {
          Phatsinh.destroy({
            where: { ma_phatsinh: id },
          });
          res.status(200).json({
            message: "Xóa khoản vay nợ thành công",
          });
        })
        .catch((err) => {
          res.status(500).json({
            message: `Xóa khoản vay nợ thất bại.`,
          });
        });
    }
    if (phatsinh.loaiphatsinh === "l" || phatsinh.loaiphatsinh === "d") {
      Vayno.update(
        {
          datra: null,
        },
        {
          where: { datra: id },
        }
      );
      Phatsinh.destroy({
        where: { ma_phatsinh: id },
      })
        .then((phatsinh) => {
          res.status(200).json({
            message: "Xóa khoản thu trả nợ thành công",
          });
        })
        .catch((err) => {
          res.status(500).json({
            message: `Xóa khoản thu trả nợ thất bại. Error: ${err}`,
          });
        });
    }
    if (phatsinh.loaiphatsinh === "c" || phatsinh.loaiphatsinh === "t") {
      Phatsinh.destroy({
        where: { ma_phatsinh: id },
      })
        .then((phatsinh) => {
          res.status(200).json({
            message: "Xóa khoản thu chi thành công",
          });
        })
        .catch((err) => {
          res.status(500).json({
            message: `Xóa khoản thu chi thất bại. Error: ${err}`,
          });
        });
    }
  } else {
    res.status(404).json({
      message: `Không tồn tại khoản phát sinh này`,
    });
  }
});

//update phatsinh
router.patch("/suathuchi/:ma_phatsinh", urlencodedParser, async (req, res) => {
  const id = req.params.ma_phatsinh;
  const { ngay, noidung, loaiphatsinh, sotien, mota, ma_taikhoan } = req.body;
  console.log(req.body);
  try {
    await Phatsinh.update(
      {
        ngay: ngay,
        noidung: noidung,
        sotien: sotien,
        mota: mota,
      },
      {
        where: { ma_phatsinh: id },
      }
    );
    await res.status(200).json({
      message: "Cập nhật khoản phát sinh thành công",
    });
  } catch (error) {
    res.status(500).json({
      message: "Cập nhật khoản phát sinh thất bại",
    });
  }
});
//update vay no
router.patch("/suavayno/:ma_phatsinh", urlencodedParser, async (req, res) => {
  const id = req.params.ma_phatsinh;
  const {
    ngay,
    noidung,
    loaiphatsinh,
    sotien,
    mota,
    ma_taikhoan,
    ngaytra,
    hoten_vayno,
    diachi_vayno,
    sdt_vayno,
  } = req.body;
  try {
    const result = await sequelize.transaction(async (t) => {
      const phatsinh = await Phatsinh.update(
        {
          ngay: ngay,
          noidung: noidung,
          sotien: sotien,
          mota: mota,
        },
        {
          where: { ma_phatsinh: id },
        },
        { transaction: t }
      );
      let vayno = await Vayno.update(
        {
          ngaytra,
          hoten_vayno,
          diachi_vayno,
          sdt_vayno,
        },
        {
          where: { ma_phatsinh: id },
        },
        { transaction: t }
      );
      if (phatsinh && vayno) {
        return res.status(200).json({
          message: "Cập nhật khoản phát sinh thành công",
        });
      } else {
        return res.status(404).json({
          message: "Không tìm thấy khoản vay nợ",
        });
      }
    });
  } catch (error) {
    res.status(500).json({
      message: "Cập nhật khoản phát sinh thất bại",
    });
  }
});
//get all cho vay chưa trả
router.get("/chovay/:id", async (req, res) => {
  try {
    const phatsinh = await Phatsinh.findAll({
      where: {
        loaiphatsinh: "v",
        ma_taikhoan: req.params.id,
      },
      order: [["ma_phatsinh", "ASC"]],
    });
    if (phatsinh.length > 0) {
      let tong = 0;
      let data = phatsinh.map((phatsinh) => phatsinh.ma_phatsinh);
      let arr = [];
      for (let i = 0; i < data.length; i++) {
        let vayno = await Vayno.findOne({
          where: { ma_phatsinh: data[i], datra: null },
        });
        if (vayno) {
          tong += Number(phatsinh[i].sotien);
          let a = {
            ma_phatsinh: phatsinh[i].ma_phatsinh,
            ngay: phatsinh[i].ngay,
            noidung: phatsinh[i].noidung,
            loaiphatsinh: phatsinh[i].loaiphatsinh,
            sotien: phatsinh[i].sotien,
            mota: phatsinh[i].mota,
            ngaytra: vayno.ngaytra,
            datra: vayno.datra,
            hoten_vayno: vayno.hoten_vayno,
            diachi_vayno: vayno.diachi_vayno,
            sdt_vayno: vayno.sdt_vayno,
          };
          arr.push(a);
        }
      }
      if (arr.length > 0)
        return res.status(200).json({
          tongvay: tong,
          chovay: arr,
        });
      else {
        res.status(404).json({
          message: "Không tồn tại cho vay chưa thu",
        });
      }
    } else {
      res.status(404).json({
        message: "Không tồn tại cho vay chưa thu",
      });
    }
  } catch (error) {
    res.status(500).json({
      message: `err ${error}`,
    });
  }
});
//get khoan no chưa trả
router.get("/khoanno/:id", async (req, res) => {
  try {
    const phatsinh = await Phatsinh.findAll({
      where: {
        loaiphatsinh: "n",
        ma_taikhoan: req.params.id,
      },
      order: [["ma_phatsinh", "ASC"]],
    });
    if (phatsinh.length > 0) {
      let tong = 0;
      let data = phatsinh.map((phatsinh) => phatsinh.ma_phatsinh);
      let arr = [];
      for (let i = 0; i < data.length; i++) {
        let vayno = await Vayno.findOne({
          where: { ma_phatsinh: data[i], datra: null },
        });
        if (vayno) {
          tong += Number(phatsinh[i].sotien);
          let a = {
            ma_phatsinh: phatsinh[i].ma_phatsinh,
            ngay: phatsinh[i].ngay,
            noidung: phatsinh[i].noidung,
            loaiphatsinh: phatsinh[i].loaiphatsinh,
            sotien: phatsinh[i].sotien,
            mota: phatsinh[i].mota,
            ngaytra: vayno.ngaytra,
            datra: vayno.datra,
            hoten_vayno: vayno.hoten_vayno,
            diachi_vayno: vayno.diachi_vayno,
            sdt_vayno: vayno.sdt_vayno,
          };
          arr.push(a);
        }
      }
      if (arr.length > 0) {
        return res.status(200).json({
          tongno: tong,
          khoanno: arr,
        });
      } else {
        res.status(404).json({
          message: "Không tồn tại khoản nợ chưa trả",
        });
      }
    } else {
      res.status(404).json({
        message: "Không tồn tại khoản nợ chưa trả",
      });
    }
  } catch (error) {
    res.status(500).json({
      message: `err ${error}`,
    });
  }
});
//getone phatsinh
router.get("/hienthi/:ma_phatsinh", async (req, res) => {
  Phatsinh.findOne(
    { where: { ma_phatsinh: req.params.ma_phatsinh } },
    {
      attributes: [
        "ma_phatsinh",
        "ngay",
        "noidung",
        "loaiphatsinh",
        "sotien",
        "mota",
        "ma_taikhoan",
      ],
    }
  )
    .then((phatsinh) => {
      if (phatsinh) {
        if (phatsinh.loaiphatsinh === "v" || phatsinh.loaiphatsinh === "n") {
          Vayno.findOne({
            where: { ma_phatsinh: req.params.ma_phatsinh },
          })
            .then((vayno) => {
              res.status(200).json({
                ma_phatsinh: phatsinh.ma_phatsinh,
                ngay: phatsinh.ngay,
                noidung: phatsinh.noidung,
                loaiphatsinh: phatsinh.loaiphatsinh,
                sotien: phatsinh.sotien,
                mota: phatsinh.mota,
                ngaytra: vayno.ngaytra,
                datra: vayno.datra,
                hoten_vayno: vayno.hoten_vayno,
                diachi_vayno: vayno.diachi_vayno,
                sdt_vayno: vayno.sdt_vayno,
                datra: vayno.datra,
              });
            })
            .catch((err) => {
              console.log(err);
            });
        } else res.status(200).json(phatsinh);
      } else {
        res.status(404).json({
          message: "Khoản phát sinh không tồn tại",
        });
      }
    })
    .catch((err) => {
      res.status(500).json({
        error: err,
      });
    });
});

//get by date
router.get("/theongay/:ngay", async (req, res) => {
  let ngay1 = req.params.ngay.split("&");
  let date = ngay1[0];
  let idtk = ngay1[1];
  try {
    const phatsinh = await Phatsinh.findAll({
      where: { ngay: date, ma_taikhoan: idtk },
      order: [["ma_phatsinh", "ASC"]],
    });
    if (phatsinh.length > 0) {
      return res.status(200).json([
        {
          ngayphatsinh: date,
          phatsinh: phatsinh,
        },
      ]);
    } else {
      return res.status(404).json({
        message: "không có khoản phát sinh nào",
      });
    }
  } catch (err) {
    return res.status(500).json({
      message: `lỗi ${err}`,
    });
  }
});
// vao ra ngay
router.get("/vaorangay/:ngay", async (req, res) => {
  let ngay1 = req.params.ngay.split("&");
  let date = ngay1[0];
  let idtk = ngay1[1];
  try {
    const phatsinh = await Phatsinh.findAll({
      where: { ngay: date, ma_taikhoan: idtk },
    });
    if (phatsinh.length > 0) {
      let moneyin = 0;
      let moneyout = 0;
      for (let i = 0; i < phatsinh.length; i++) {
        if (
          phatsinh[i].loaiphatsinh === "t" ||
          phatsinh[i].loaiphatsinh === "n" ||
          phatsinh[i].loaiphatsinh === "l"
        ) {
          moneyin += Number(phatsinh[i].sotien);
        } else {
          moneyout += Number(phatsinh[i].sotien);
        }
      }
      return res.status(200).json({
        tienvao: moneyin,
        tienra: moneyout,
      });
    } else {
      return res.status(404).json({
        message: "không có khoản phát sinh nào",
      });
    }
  } catch (err) {
    return res.status(500).json({
      message: `lỗi ${err}`,
    });
  }
});
// get money in out
router.get("/tienvaora/:khoang", async (req, res) => {
  const khoang = req.params.khoang.split("&");
  const from = khoang[0];
  const to = khoang[1];
  const idtk = khoang[2];
  try {
    const phatsinh = await Phatsinh.findAll({
      attributes: ["ngay", "sotien", "loaiphatsinh"],
      raw: true,
      where: {
        ma_taikhoan: idtk,
        ngay: {
          [Op.between]: [from, to],
        },
      },
    });
    if (phatsinh.length > 0) {
      let moneyin = 0;
      let moneyout = 0;
      for (let i = 0; i < phatsinh.length; i++) {
        if (
          phatsinh[i].loaiphatsinh === "t" ||
          phatsinh[i].loaiphatsinh === "n" ||
          phatsinh[i].loaiphatsinh === "l"
        ) {
          moneyin += Number(phatsinh[i].sotien);
        } else {
          moneyout += Number(phatsinh[i].sotien);
        }
      }
      return res.status(200).json({
        tienvao: moneyin,
        tienra: moneyout,
      });
      //  console.log(moneyin, moneyout)
    } else {
      return res.status(404).json({
        message: `Không có khoản phát sinh nào`,
      });
    }
  } catch (err) {
    return res.status(500).json({
      message: `Lỗi ${err}`,
    });
  }
});
//get by interval of time
router.get("/khoangthoigian/:khoang", async (req, res) => {
  const khoang = req.params.khoang.split("&");
  const from = khoang[0];
  const to = khoang[1];
  const idtk = khoang[2];
  Phatsinh.findAll({
    attributes: ["ngay"],
    raw: true,
    group: ["ngay"],
    where: {
      ma_taikhoan: idtk,
      ngay: {
        [Op.between]: [from, to],
      },
    },
    order: [["ngay", "ASC"]],
  })
    .then(async (phatsinh) => {
      if (phatsinh.length > 0) {
        const arr = await phatsinh.map((phatsinh) => phatsinh.ngay);
        const data2 = [];
        for (let i = 0; i < arr.length; i++) {
          const phatsinhs = await Phatsinh.findAll({
            raw: true,
            where: { ngay: arr[i], ma_taikhoan: idtk },
            order: [["ma_phatsinh", "ASC"]],
          });
          const a = {
            ngayphatsinh: arr[i],
            phatsinh: phatsinhs,
          };
          data2.push(a);
        }
        return res.status(200).json(data2);
      } else {
        return res.status(404).json({
          message: "Không có khoản phát sinh nào",
        });
      }
    })
    .catch((err) => {
      return res.status(500).json({
        message: `Lỗi ${err}`,
      });
    });
});
//get by month
router.get("/theothang/:ngay", async (req, res) => {
  const ngay = req.params.ngay.split("&");
  const a = ngay[0].split("-");
  const fromMonth = a[1];
  const fromYear = a[0];
  const idtk = ngay[1];
  Phatsinh.findAll({
    attributes: ["ngay"],
    raw: true,
    group: ["ngay"],
    where: {
      ma_taikhoan: idtk,
      [Op.and]: [
        sequelize.where(
          sequelize.literal(`extract (month from ("ngay"))`),
          fromMonth
        ),
        sequelize.where(
          sequelize.literal(`extract (year from ("ngay"))`),
          fromYear
        ),
      ],
    },
    order: [["ngay", "ASC"]],
  })
    .then(async (phatsinh) => {
      if (phatsinh.length > 0) {
        const arr = await phatsinh.map((phatsinh) => phatsinh.ngay);
        const data2 = [];
        for (let i = 0; i < arr.length; i++) {
          const phatsinhs = await Phatsinh.findAll({
            raw: true,
            where: { ngay: arr[i], ma_taikhoan: idtk },
            order: [["ma_phatsinh", "ASC"]],
          });
          const a = {
            ngayphatsinh: arr[i],
            phatsinh: phatsinhs,
          };
          data2.push(a);
        }
        return res.status(200).json(data2);
      } else {
        return res.status(404).json({
          message: "Không có khoản phát sinh nào",
        });
      }
    })
    .catch((err) => {
      return res.status(500).json({
        message: `Lỗi ${err}`,
      });
    });
});
//get tien vao ra thang
router.get("/vaorathang/:ngay", async (req, res) => {
  const ngay = req.params.ngay.split("&");
  const a = ngay[0].split("-");
  const fromMonth = a[1];
  const fromYear = a[0];
  const idtk = ngay[1];
  try {
    const phatsinh = await Phatsinh.findAll({
      attributes: ["ngay", "sotien", "loaiphatsinh"],
      raw: true,
      where: {
        ma_taikhoan: idtk,
        [Op.and]: [
          sequelize.where(
            sequelize.literal(`extract (month from ("ngay"))`),
            fromMonth
          ),
          sequelize.where(
            sequelize.literal(`extract (year from ("ngay"))`),
            fromYear
          ),
        ],
      },
    });
    if (phatsinh.length > 0) {
      let moneyin = 0;
      let moneyout = 0;
      for (let i = 0; i < phatsinh.length; i++) {
        if (
          phatsinh[i].loaiphatsinh === "t" ||
          phatsinh[i].loaiphatsinh === "n" ||
          phatsinh[i].loaiphatsinh === "l"
        ) {
          moneyin += Number(phatsinh[i].sotien);
        } else {
          moneyout += Number(phatsinh[i].sotien);
        }
      }
      return res.status(200).json({
        tienvao: moneyin,
        tienra: moneyout,
      });
      //  console.log(moneyin, moneyout)
    } else {
      return res.status(404).json({
        message: `Không có khoản phát sinh nào`,
      });
    }
  } catch (err) {
    return res.status(500).json({
      message: `Lỗi ${err}`,
    });
  }
});
//vaoranam
router.get("/vaoranam/:ngay", async (req, res) => {
  const ngay = req.params.ngay.split("&");

  const fromYear = ngay[0];
  const idtk = ngay[1];
  try {
    const phatsinh = await Phatsinh.findAll({
      attributes: ["ngay", "sotien", "loaiphatsinh"],
      raw: true,
      where: {
        ma_taikhoan: idtk,
        [Op.and]: [
          sequelize.where(
            sequelize.literal(`extract (year from ("ngay"))`),
            fromYear
          ),
        ],
      },
    });
    if (phatsinh.length > 0) {
      let moneyin = 0;
      let moneyout = 0;
      for (let i = 0; i < phatsinh.length; i++) {
        if (
          phatsinh[i].loaiphatsinh === "t" ||
          phatsinh[i].loaiphatsinh === "n" ||
          phatsinh[i].loaiphatsinh === "l"
        ) {
          moneyin += Number(phatsinh[i].sotien);
        } else {
          moneyout += Number(phatsinh[i].sotien);
        }
      }
      return res.status(200).json({
        tienvao: moneyin,
        tienra: moneyout,
      });
      //  console.log(moneyin, moneyout)
    } else {
      return res.status(404).json({
        message: `Không có khoản phát sinh nào`,
      });
    }
  } catch (err) {
    return res.status(500).json({
      message: `Lỗi ${err}`,
    });
  }
});
//get by year
router.get("/theonam/:ngay", async (req, res) => {
  const ngay = req.params.ngay.split("&");
  const Year0 = ngay[0];
  const idtk = ngay[1];
  Phatsinh.findAll({
    attributes: ["ngay"],
    raw: true,
    where: {
      ma_taikhoan: idtk,
      [Op.and]: [
        sequelize.where(
          sequelize.literal(`extract (year from ("ngay"))`),
          Year0
        ),
      ],
    },
    order: [["ngay", "ASC"]],
  })
    .then(async (phatsinh) => {
      if (phatsinh.length > 0) {
        const arr = await phatsinh.map((phatsinh) => {
          let fromDateMonth = new Date(phatsinh.ngay);
          let fromMonth = fromDateMonth.getMonth() + 1;
          let fromYear = fromDateMonth.getUTCFullYear();
          return `${fromYear}-${fromMonth}`;
        });
        const arr1 = await arr.filter((a, b) => arr.indexOf(a) === b);
        console.log(arr1);
        const data2 = [];
        for (let i = 0; i < arr1.length; i++) {
          let ngay1 = arr1[i].split("-");
          let Month = ngay1[1];
          let Year = ngay1[0];
          const phatsinhs = await Phatsinh.findAll({
            raw: true,
            where: {
              ma_taikhoan: idtk,
              [Op.and]: [
                sequelize.where(
                  sequelize.literal(`extract (month from ("ngay"))`),
                  Month
                ),
                sequelize.where(
                  sequelize.literal(`extract (year from ("ngay"))`),
                  Year
                ),
              ],
            },
            order: [["ma_phatsinh", "ASC"]],
          });
          const a = {
            ngayphatsinh: arr1[i],
            phatsinh: phatsinhs,
          };
          data2.push(a);
        }
        return res.status(200).json(data2);
      } else {
        return res.status(404).json({
          message: "Không có khoản phát sinh nào",
        });
      }
    })
    .catch((err) => {
      return res.status(500).json({
        message: `Lỗi ${err}`,
      });
    });
});
// vao ra all
router.get("/vaoratatca/:id", async (req, res) => {
  const idtk = req.params.id;
  try {
    const phatsinh = await Phatsinh.findAll({
      attributes: ["ngay", "sotien", "loaiphatsinh"],
      raw: true,
      where: { ma_taikhoan: idtk },
    });
    if (phatsinh.length > 0) {
      let moneyin = 0;
      let moneyout = 0;
      for (let i = 0; i < phatsinh.length; i++) {
        if (
          phatsinh[i].loaiphatsinh === "t" ||
          phatsinh[i].loaiphatsinh === "n" ||
          phatsinh[i].loaiphatsinh === "l"
        ) {
          moneyin += Number(phatsinh[i].sotien);
        } else {
          moneyout += Number(phatsinh[i].sotien);
        }
      }
      return res.status(200).json({
        tienvao: moneyin,
        tienra: moneyout,
      });
      //  console.log(moneyin, moneyout)
    } else {
      return res.status(404).json({
        message: `Không có khoản phát sinh nào`,
      });
    }
  } catch (err) {
    return res.status(500).json({
      message: `Lỗi ${err}`,
    });
  }
});
//get all phat sinh
router.get("/tatca/:id", async (req, res) => {
  const idtk = req.params.id;
  Phatsinh.findAll({
    attributes: ["ngay"],
    raw: true,
    where: { ma_taikhoan: idtk },
    order: [["ma_phatsinh", "ASC"]],
  })
    .then(async (phatsinh) => {
      if (phatsinh.length > 0) {
        const arr = await phatsinh.map((phatsinh) => {
          let fromDateMonth = new Date(phatsinh.ngay);
          let fromMonth = fromDateMonth.getMonth() + 1;
          let fromYear = fromDateMonth.getUTCFullYear();
          return `${fromYear}-${fromMonth}`;
        });
        const arr1 = await arr.filter((a, b) => arr.indexOf(a) === b);
        console.log(arr1);
        const data2 = [];
        for (let i = 0; i < arr1.length; i++) {
          let ngay1 = arr1[i].split("-");
          let Month = ngay1[1];
          let Year = ngay1[0];
          const phatsinhs = await Phatsinh.findAll({
            raw: true,
            where: {
              ma_taikhoan: idtk,
              [Op.and]: [
                sequelize.where(
                  sequelize.literal(`extract (month from ("ngay"))`),
                  Month
                ),
                sequelize.where(
                  sequelize.literal(`extract (year from ("ngay"))`),
                  Year
                ),
              ],
            },
            order: [["ma_phatsinh", "ASC"]],
          });
          const a = {
            ngayphatsinh: arr1[i],
            phatsinh: phatsinhs,
          };
          data2.push(a);
        }
        return res.status(200).json(data2);
      } else {
        return res.status(404).json({
          message: "Không có khoản phát sinh nào",
        });
      }
    })
    .catch((err) => {
      return res.status(500).json({
        message: `Lỗi ${err}`,
      });
    });
});

//get số dư đầu cuối của ngày
router.get("/baocaongay/:ngay", async (req, res) => {
  let ngay1 = req.params.ngay.split("&");
  let date = ngay1[0];
  let idtk = ngay1[1];
  try {
    const taikhoan = await Taikhoan.findOne({
      attributes: ["sodu"],
      raw: true,
      where: {
        ma_taikhoan: idtk,
      },
    });
    const phatsinh = await Phatsinh.findAll({
      attributes: ["loaiphatsinh", "sotien"],
      raw: true,
      where: {
        ngay: {
          [Op.gte]: date,
        },
        ma_taikhoan: idtk,
      },
    });
    let thu = 0;
    let chi = 0;
    for (let i = 0; i < phatsinh.length; i++) {
      if (
        phatsinh[i].loaiphatsinh === "t" ||
        phatsinh[i].loaiphatsinh === "n" ||
        phatsinh[i].loaiphatsinh === "l"
      ) {
        thu += Number(phatsinh[i].sotien);
      } else {
        chi += Number(phatsinh[i].sotien);
      }
    }
    let tong = thu - chi;
    //  tong = Number(thu - chi);
    //  console.log(thu, chi);
    const phatsinh1 = await Phatsinh.findAll({
      attributes: ["loaiphatsinh", "sotien"],
      where: { ngay: date, ma_taikhoan: idtk },
    });
    let thu1 = 0;
    let chi1 = 0;
    let vay1 = 0;
    let no1 = 0;
    let lay1 = 0;
    let tra1 = 0;
    for (let i = 0; i < phatsinh1.length; i++) {
      if (phatsinh1[i].loaiphatsinh === "t") {
        thu1 += Number(phatsinh1[i].sotien);
      }
      if (phatsinh1[i].loaiphatsinh === "c") {
        chi1 += Number(phatsinh1[i].sotien);
      }
      if (phatsinh1[i].loaiphatsinh === "v") {
        vay1 += Number(phatsinh1[i].sotien);
      }
      if (phatsinh1[i].loaiphatsinh === "n") {
        no1 += Number(phatsinh1[i].sotien);
      }
      if (phatsinh1[i].loaiphatsinh === "l") {
        lay1 += Number(phatsinh1[i].sotien);
      }
      if (phatsinh1[i].loaiphatsinh === "d") {
        tra1 += Number(phatsinh1[i].sotien);
      }
    }
    let tong1 = Number(thu1 + no1 + lay1 - (chi1 + vay1 + tra1));
    return res.status(200).json({
      sodudau: Number(taikhoan.sodu - tong),
      soducuoi: Number(taikhoan.sodu - tong) + tong1,
      thu: thu1,
      chi: chi1,
      vay: vay1,
      no: no1,
      lay: lay1,
      tra: tra1,
    });
  } catch (err) {
    return res.status(500).json({
      message: `lỗi ${err}`,
    });
  }
});
router.get("/baocaothang/:ngay", async (req, res) => {
  let ngay1 = req.params.ngay.split("&");
  let date = ngay1[0];
  let idtk = ngay1[1];
  try {
    const taikhoan = await Taikhoan.findOne({
      attributes: ["sodu"],
      raw: true,
      where: {
        ma_taikhoan: idtk,
      },
    });
    let ngay2 = date.split("-");
    let Month = ngay2[1];
    let Year = ngay2[0];
    const phatsinh = await Phatsinh.findAll({
      attributes: ["loaiphatsinh", "sotien"],
      raw: true,
      where: {
        ma_taikhoan: idtk,
        [Op.and]: [
          sequelize.where(sequelize.literal(`extract (month from ("ngay"))`), {
            [Op.gte]: Month,
          }),
          sequelize.where(sequelize.literal(`extract (year from ("ngay"))`), {
            [Op.gte]: Year,
          }),
        ],
      },
    });
    let thu = 0;
    let chi = 0;
    for (let i = 0; i < phatsinh.length; i++) {
      if (
        phatsinh[i].loaiphatsinh === "t" ||
        phatsinh[i].loaiphatsinh === "n" ||
        phatsinh[i].loaiphatsinh === "l"
      ) {
        thu += Number(phatsinh[i].sotien);
      } else {
        chi += Number(phatsinh[i].sotien);
      }
    }
    let tong = thu - chi;

    const phatsinh1 = await Phatsinh.findAll({
      attributes: ["loaiphatsinh", "sotien"],
      where: {
        ma_taikhoan: idtk,
        [Op.and]: [
          sequelize.where(
            sequelize.literal(`extract (month from ("ngay"))`),
            Month
          ),
          sequelize.where(
            sequelize.literal(`extract (year from ("ngay"))`),
            Year
          ),
        ],
      },
    });
    let thu1 = 0;
    let chi1 = 0;
    let vay1 = 0;
    let no1 = 0;
    let lay1 = 0;
    let tra1 = 0;
    for (let i = 0; i < phatsinh1.length; i++) {
      if (phatsinh1[i].loaiphatsinh === "t") {
        thu1 += Number(phatsinh1[i].sotien);
      }
      if (phatsinh1[i].loaiphatsinh === "c") {
        chi1 += Number(phatsinh1[i].sotien);
      }
      if (phatsinh1[i].loaiphatsinh === "v") {
        vay1 += Number(phatsinh1[i].sotien);
      }
      if (phatsinh1[i].loaiphatsinh === "n") {
        no1 += Number(phatsinh1[i].sotien);
      }
      if (phatsinh1[i].loaiphatsinh === "l") {
        lay1 += Number(phatsinh1[i].sotien);
      }
      if (phatsinh1[i].loaiphatsinh === "d") {
        tra1 += Number(phatsinh1[i].sotien);
      }
    }
    let tong1 = Number(thu1 + no1 + lay1 - (chi1 + vay1 + tra1));
    return res.status(200).json({
      sodudau: Number(taikhoan.sodu - tong),
      soducuoi: Number(taikhoan.sodu - tong) + tong1,
      thu: thu1,
      chi: chi1,
      vay: vay1,
      no: no1,
      lay: lay1,
      tra: tra1,
    });
  } catch (err) {
    return res.status(500).json({
      message: `lỗi ${err}`,
    });
  }
});
router.get("/baocaonam/:ngay", async (req, res) => {
  let ngay1 = req.params.ngay.split("&");
  let date = ngay1[0];
  let idtk = ngay1[1];
  try {
    const taikhoan = await Taikhoan.findOne({
      attributes: ["sodu"],
      raw: true,
      where: {
        ma_taikhoan: idtk,
      },
    });
    const phatsinh = await Phatsinh.findAll({
      attributes: ["loaiphatsinh", "sotien"],
      raw: true,
      where: {
        ma_taikhoan: idtk,
        [Op.and]: [
          sequelize.where(sequelize.literal(`extract (year from ("ngay"))`), {
            [Op.gte]: date,
          }),
        ],
      },
    });
    let thu = 0;
    let chi = 0;
    for (let i = 0; i < phatsinh.length; i++) {
      if (
        phatsinh[i].loaiphatsinh === "t" ||
        phatsinh[i].loaiphatsinh === "n" ||
        phatsinh[i].loaiphatsinh === "l"
      ) {
        thu += Number(phatsinh[i].sotien);
      } else {
        chi += Number(phatsinh[i].sotien);
      }
    }
    let tong = thu - chi;

    const phatsinh1 = await Phatsinh.findAll({
      attributes: ["loaiphatsinh", "sotien"],
      where: {
        ma_taikhoan: idtk,
        [Op.and]: [
          sequelize.where(
            sequelize.literal(`extract (year from ("ngay"))`),
            date
          ),
        ],
      },
    });
    let thu1 = 0;
    let chi1 = 0;
    let vay1 = 0;
    let no1 = 0;
    let lay1 = 0;
    let tra1 = 0;
    for (let i = 0; i < phatsinh1.length; i++) {
      if (phatsinh1[i].loaiphatsinh === "t") {
        thu1 += Number(phatsinh1[i].sotien);
      }
      if (phatsinh1[i].loaiphatsinh === "c") {
        chi1 += Number(phatsinh1[i].sotien);
      }
      if (phatsinh1[i].loaiphatsinh === "v") {
        vay1 += Number(phatsinh1[i].sotien);
      }
      if (phatsinh1[i].loaiphatsinh === "n") {
        no1 += Number(phatsinh1[i].sotien);
      }
      if (phatsinh1[i].loaiphatsinh === "l") {
        lay1 += Number(phatsinh1[i].sotien);
      }
      if (phatsinh1[i].loaiphatsinh === "d") {
        tra1 += Number(phatsinh1[i].sotien);
      }
    }
    let tong1 = Number(thu1 + no1 + lay1 - (chi1 + vay1 + tra1));
    return res.status(200).json({
      sodudau: Number(taikhoan.sodu - tong),
      soducuoi: Number(taikhoan.sodu - tong) + tong1,
      thu: thu1,
      chi: chi1,
      vay: vay1,
      no: no1,
      lay: lay1,
      tra: tra1,
    });
  } catch (err) {
    return res.status(500).json({
      message: `lỗi ${err}`,
    });
  }
});
router.get("/thongbao/:ngay", async (req, res) => {
  let ngay1 = req.params.ngay.split("&");
  let date1 = ngay1[0];
  const d = new Date(date1).toISOString();
  let idtk = ngay1[1];
  try {
    const phatsinh = await Phatsinh.findAll({
      where: {
        loaiphatsinh: ["n", "v"],
        ma_taikhoan: idtk,
      },
    });
    if (phatsinh.length > 0) {
      let tong = 0;
      let data = phatsinh.map((phatsinh) => phatsinh.ma_phatsinh);
      for (let i = 0; i < data.length; i++) {
        let vayno = await Vayno.findOne({
          where: {
            ngaytra: {
              [Op.lte]: d,
            },
            ma_phatsinh: data[i],
            datra: null,
          },
        });
        if (vayno) {
          tong += 1;
        }
      }
      return res.status(200).json({
        thongbao: tong,
      });
    } else {
      return res.status(404).json({
        message: "Không tìm thấy",
      });
    }
  } catch (error) {
    res.status(500).json({
      message: `err ${error}`,
    });
  }
});
router.get("/kiemtra/:id", async (req, res) => {
  try {
    let vayno = await Vayno.findOne({
      where: {
        ma_phatsinh: req.params.id,
        datra: { [Op.ne]: null },
      },
    });
    if (vayno) {
      return res.status(200).json({
        message: "ok",
      });
    } else {
      return res.status(404).json({
        message: "Không tìm thấy",
      });
    }
  } catch (error) {
    res.status(500).json({
      message: `err ${error}`,
    });
  }
});
module.exports = router;
