const Sequelize = require('sequelize');
const sequelize = require('../database/database');
const Op = Sequelize.Op;
const Taikhoan = require('./taikhoan');

const Phatsinh = sequelize.define('phatsinh',{
    ma_phatsinh:{
        type: Sequelize.INTEGER,
        primaryKey: true
    },
    ngay:{
        type: Sequelize.DATEONLY
    },
    noidung:{
        type: Sequelize.STRING
    },
    loaiphatsinh:{
        type: Sequelize.CHAR
    },
    sotien:{
        type: Sequelize.DOUBLE
    },
    mota:{
        type: Sequelize.STRING
    },
    ma_taikhoan:{
        type: Sequelize.INTEGER
    },

},{
    timestamps: false,
    freezeTableName: true
});

Taikhoan.hasMany(Phatsinh, { foreignKey:'ma_taikhoan', sourceKey:'ma_taikhoan'});
Phatsinh.belongsTo(Taikhoan, { foreignKey:'ma_taikhoan', targetKey:'ma_taikhoan'});
module.exports = Phatsinh;

