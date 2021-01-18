const jwt =  require('jsonwebtoken');

const (req, res, next) => {
    try {
        const token = req.headers.authorization;
        const decoded = jwt.verify(token, process.env.JWT_KEY);
        res.userData = decoded;
        console.log(decoded);
        next();
    } catch (error) {
        return res.status(401).json({
            message : "Auth failed"
        })
    }
};