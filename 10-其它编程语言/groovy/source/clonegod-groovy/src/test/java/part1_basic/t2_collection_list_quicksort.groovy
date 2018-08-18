package part1_basic

/**
 * 快速排序
 * 目标是使用较少的比较，这个策略依赖于在list中找到一个好的核心元素用来把list拆分为两个list：
 * 一个list中的所有元素都比核心元素小，另外一个list中的所有元素都比核心元素大，然后再递归调用这两个list，
 * 
 * 这后面的理论基础是不需要把一个列表的元素和另一个列表进行比较，如果总能找到正确的核心元素，这将精确的对半拆分list，
 * 这个算法运行一个复杂的n*log(n)，
 * 最坏的结果，每次都选择了一个边界元素作为核心元素，那么排序将以n2结束
 * 
 */
def quickSort(list) {
	if (list.size() < 2) return list
	// classify by pivot
	def pivot = list[list.size().intdiv(2)]
	def left = list.findAll { item -> item < pivot }
	def middle = list.findAll {	item -> item == pivot }
	def right = list.findAll {	item -> item > pivot }
	// recursive calls
	return (quickSort(left) + middle + quickSort(right))
}

assert quickSort([]) == [];
assert quickSort([1]) == [1];
assert quickSort([1,2]) == [1,2];
assert quickSort([2,1]) == [1,2];
assert quickSort([3,1,2]) == [1,2,3];
assert quickSort([3,1,2,2]) == [1,2,2,3];
assert quickSort([1.0f, 'a', 10, null]) == [null, 1.0, 10, 'a'];
