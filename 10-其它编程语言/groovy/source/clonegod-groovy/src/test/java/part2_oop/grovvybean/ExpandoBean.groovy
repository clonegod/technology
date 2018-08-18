package part2_oop.grovvybean

/**
 * Expando 对象通过赋值方式扩展属性，这与 map 相似，不同的时候将闭包赋值给一个属性，这样在访问属性的时候闭包被执行，闭包可以接受任意参数。
 * 
 * 在这个例子中， boxer的 fightBack 按给定的倍数返回
 */
def boxer = new Expando()

assert null == boxer.takeThis

boxer.takeThis = 'ouch!'
assert 'ouch!' == boxer.takeThis

// 在某种程度上， Expando 将闭包分配给属性的行为和通过属性访问存储的闭包有点像动态的增加方法给对象。
boxer.fightBack = {times -> return boxer.takeThis * times}
assert 'ouch!ouch!ouch!' == boxer.fightBack(3)
