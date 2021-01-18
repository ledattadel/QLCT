const Sequelize = require('sequelize');
const sequelize = require('../database/database');
const Op = Sequelize.Op;
const Phatsinh = require('./phatsinh');

const Vayno = sequelize.define('vayno',{
    ma_vayno:{
        type: Sequelize.INTEGER,
        primaryKey: true
    },
    ngaytra:{
        type: Sequelize.DATE
    },
    datra:{
        type: Sequelize.INTEGER
    },
    hoten_vayno:{
        type: Sequelize.STRING
    },
    diachi_vayno:{
        type: Sequelize.STRING
    },
    sdt_vayno:{
        type: Sequelize.STRING
    },
    ma_phatsinh:{
        type: Sequelize.INTEGER
    },

},{
    timestamps: false,
    freezeTableName: true
});

Phatsinh.hasMany(Vayno, { foreignKey:'ma_phatsinh', sourceKey:'ma_phatsinh'});
Vayno.belongsTo(Phatsinh, { foreignKey:'ma_phatsinh', targetKey:'ma_phatsinh'});
module.exports = Vayno;
