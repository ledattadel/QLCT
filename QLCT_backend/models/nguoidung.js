const Sequelize = require('sequelize');
const sequelize = require('../database/database');
const Op = Sequelize.Op;

const Nguoidung = sequelize.define('nguoidung', {
    tennguoidung:{
        type: Sequelize.STRING,
        primaryKey: true
    },
    matkhau:{
        type: Sequelize.STRING
    },
    ma_nguoidung:{
        type: Sequelize.INTEGER
    }
} ,{
    timestamps: false,
    freezeTableName: true
});
module.exports = Nguoidung;

