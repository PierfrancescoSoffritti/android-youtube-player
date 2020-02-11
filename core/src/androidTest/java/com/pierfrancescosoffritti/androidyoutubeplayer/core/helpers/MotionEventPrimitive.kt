package com.pierfrancescosoffritti.androidyoutubeplayer.core.helpers

/**
 * This class is needed to export MotionEvent attributes from Activity to the TestClasses.
 * Using the MotionEvent directly is not possible since it get overwritten by internal
 * code before it reaches the TestClass
 *
 * @constructor Creates an initialized MotionEvent
 */
class MotionEventPrimitive{

    constructor(X:Float,Y:Float,action:Int){
        this.X = X
        this.Y = Y
        this.action = action
    }

    var X = -1F
    var Y = -1F
    var action = -1
}