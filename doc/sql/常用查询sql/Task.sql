db.emulate_trade_task.createIndex({last_profit: 1});

// 修改 emulate_trade_task 中的 last_profit 字段的类型为 Decimal128

db.emulate_trade_task.find({status: "SUCCESS"}).count();

db.emulate_trade_task.find({
    "last_profit": {$gte: NumberDecimal("125.00")}
}).sort({last_profit: -1});

