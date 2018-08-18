The principal advantage of B+ trees over B trees is they allow you to pack in more pointers to other nodes by removing pointers to data, thus increasing the fanout and potentially decreasing the depth of the tree.

The disadvantage is that there are no early outs when you might have found a match in an internal node. 

But since both data structures have huge fanouts, the vast majority of your matches will be on leaf nodes anyway, making on average the B+ tree more efficient.

---
B+Trees are much easier and higher performing to do a full scan, as in look at every piece of data that the tree indexes, since the terminal nodes form a linked list. 

To do a full scan with a B-Tree you need to do a full tree traversal to find all the data.

B-Trees on the other hand can be faster when you do a seek (looking for a specific piece of data by key) especially when the tree resides in RAM or other non-block storage. 

Since you can elevate commonly used nodes in the tree there are less comparisons required to get to the data.

---
B+ Trees are different from B Trees with following two properties:

	B+ trees don't store data pointer in interior nodes, they are ONLY stored in leaf nodes. 
	This is not optional as in B-Tree. 
	This means that interior nodes can fit more keys on block of memory.
	
	The leaf nodes of B+ trees are linked, so doing a linear scan of all keys will requires just one pass through all the leaf nodes. 
	A B tree, on the other hand, would require a traversal of every level in the tree. 
	This property can be utilized for efficient search as well since data is stored only in leafs.

	With no data in the interior nodes the fan-out can be higher than with a B tree, tree depth shorter, 
	reads from secondary storage lower, 
	cache hit rate on intermediate nodes better, 
	and time to data faster.

---
The image below helps show the differences between B+ trees and B trees.

#######Advantages of B+ trees:

	Because B+ trees don't have data associated with interior nodes, more keys can fit on a page of memory. 
	Therefore, it will require fewer cache misses in order to access data that is on a leaf node.
	The leaf nodes of B+ trees are linked, so doing a full scan of all objects in a tree requires just one linear pass through all the leaf nodes. 
	A B tree, on the other hand, would require a traversal of every level in the tree. This full-tree traversal will likely involve more cache misses than the linear traversal of B+ leaves.

######Advantage of B trees:

	Because B trees contain data with each key, frequently accessed nodes can lie closer to the root, and therefore can be accessed more quickly.



>>>MORE
	
	https://en.wikipedia.org/wiki/B-tree
	https://en.wikipedia.org/wiki/B%2B_tree
	https://techdifferences.com/difference-between-b-tree-and-binary-tree.html

