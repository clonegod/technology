package part1_basic

import groovy.xml.StaxBuilder

def textCorpus= """
Look for the bare necessities
The simple bare necessities
Forget about your worries and your strife
I mean the bare necessities
Old Mother Nature's recipes
That bring the bare necessities of life
"""

// 统计单词
def words = textCorpus.tokenize()
def wordFrequency = [:]
words.each { word -> 
	wordFrequency[word] = wordFrequency.get(word, 0) + 1
}

// 按按单词的频次排序，默认升序
def wordList = wordFrequency.keySet().toList()
//wordList.sort { it-> wordFrequency[it] } 
wordList.sort { wordFrequency[it] } 

// 生成统计结果
def statistic = "\n"
wordList[-1..-6].each { word ->
	statistic += word.padLeft(12) + ": "
	statistic += wordFrequency[word] + "\n"
}

println statistic