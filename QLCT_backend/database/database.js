const Sequelize = require('sequelize');
const sequelize = new Sequelize(
    'qlchitieu',
    'postgres',
    '123456',
    {
    dialect: 'postgres',
    host: 'localhost',
    port: 5432,
    pool: {
            max: 5,
            min: 0,
            require:30000,
            idle: 10000
        }
    }
);
module.exports = sequelize

sequelize.authenticate()
.then(() => console.log('ket noi thanh cong!'))
.catch(err => console.log(err.message))
