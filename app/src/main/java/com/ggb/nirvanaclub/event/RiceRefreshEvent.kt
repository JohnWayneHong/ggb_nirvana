package com.ggb.nirvanaclub.event

class RiceRefreshEvent (
    var minStep:Int,
    var walkRiceGrains:String,
    var drivingRiceGrains:String,
    var rotateMinStep:Int,
    var targetWalk:Int,
    var mileage:Float,
    var targetDriving:Float
)