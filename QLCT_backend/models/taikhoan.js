const Sequelize = require('sequelize');
const sequelize = require('../database/database');
const Op = Sequelize.Op;
const Thongtin_nguoidung = require('./thongtin_nguoidung');

const Taikhoan = sequelize.define('taikhoan',{
    ma_taikhoan:{
        type: Sequelize.INTEGER,
        primaryKey: true
    },
    sodu:{
        type: Sequelize.DOUBLE
    }
},{
    timestamps: false,
    freezeTableName: true
});

Thongtin_nguoidung.hasMany(Taikhoan, { foreignKey:'ma_taikhoan', sourceKey:'ma_taikhoan'});
Taikhoan.belongsTo(Thongtin_nguoidung, { foreignKey:'ma_taikhoan', targetKey:'ma_taikhoan'});
module.exports = Taikhoan;
