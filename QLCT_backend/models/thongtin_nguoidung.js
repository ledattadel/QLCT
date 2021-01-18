const Sequelize = require('sequelize');
const sequelize = require('../database/database');
const Op = Sequelize.Op;
const nguoidung = require('./nguoidung');


const Thongtin_nguoidung = sequelize.define('thongtin_nguoidung',{
    ma_nguoidung:{
        type: Sequelize.INTEGER,
        primaryKey: true
    },
    hoten:{
        type: Sequelize.STRING
    },
    gioitinh:{
        type: Sequelize.CHAR
    },
    sodienthoai:{
        type: Sequelize.STRING
    },
    diachi:{
        type: Sequelize.STRING
    },
    nghenghiep:{
        type: Sequelize.STRING
    },
    ma_taikhoan:{
        type: Sequelize.INTEGER
    }
},{
    timestamps: false,
    freezeTableName: true
});
nguoidung.hasMany(Thongtin_nguoidung, { foreignKey:'ma_nguoidung', sourceKey:'ma_nguoidung'});
Thongtin_nguoidung.belongsTo(nguoidung, { foreignKey:'ma_nguoidung', targetKey:'ma_nguoidung'});
module.exports = Thongtin_nguoidung;



