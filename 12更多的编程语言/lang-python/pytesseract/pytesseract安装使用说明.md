##【安装tesseract, 中文字库（可选）】
https://github.com/tesseract-ocr/tesseract/wiki

https://github.com/tesseract-ocr/tesseract/wiki/Downloads

##非官方windows安装包 4.0.0-alpha for Windows
    http://digi.bib.uni-mannheim.de/tesseract/tesseract-ocr-setup-4.00.00dev.exe
    安装到：C:\Program Files (x86)\Tesseract-OCR
    

##中文语言包下载
（下载后放到安装tessertact的目录下：%Tesseract-OCR%/tessdata）

    https://github.com/tesseract-ocr/tesseract/wiki/Data-Files
    chi_sim     Chinese - Simplified        https://github.com/tesseract-ocr/tessdata/raw/4.00/chi_sim.traineddata
    chi_tra     Chinese - Traditional       https://github.com/tesseract-ocr/tessdata/raw/4.00/chi_tra.traineddata
    

	C:\Users\Administrator.CE-20160511RDFS>tesseract -v
	tesseract 4.00.00alpha
	 leptonica-1.74.1
	  libgif 4.1.6(?) : libjpeg 8d (libjpeg-turbo 1.5.0) : libpng 1.6.20 : libtiff 4.0.6 : zlib 1.2.8 : libwebp 0.4.3 : libopenjp2 2.1.0    
  
  
 

##【测试tesseract可用性】
	---自定义白名单，限定目标字符集
	C:\Program Files (x86)\Tesseract-OCR\tessdata\configs
    
    my-digits
            tessedit_char_whitelist 0123456789
            
    my-chars    
        tessedit_char_whitelist ABCDEFGHIJKLMNOPQRSTUVWXYZ

        
	---识别数字，设置白名单为my-digit
    tesseract num-row2.png output -l eng --psm 13 --oem 0 my-digit

	---识别英文字母（大写），设置白名单为my-chars
    tesseract num-row1.png output -l eng --psm 13 --oem 0 my-chars


	#  --psm 13 :   Raw line. Treat the image as a single text line 图片按行分割，按行进行解析
	#  --oem 0 :    Original Tesseract only  选择OCR Engine的类型为原始类型，其它类型不支持白名单解析
	#  my-digits：  配置图片的白名单（数字或大写字母）

	F:\python-web\pytesseract\img> tesseract num-row1.png output -l eng --psm 13 --oem 0 my-digits
	F:\python-web\pytesseract\img> tesseract num-row2.png output -l eng --psm 13 --oem 0 my-digits

	F:\python-web\pytesseract\img> tesseract upper-row1.png output -l eng --psm 13 --oem 0 my-chars
	F:\python-web\pytesseract\img> tesseract upper-row1.png output -l eng --psm 13 --oem 0 my-chars
	F:\python-web\pytesseract\img> tesseract upper-row1.png output -l eng --psm 13 --oem 0 my-chars

## 【tesseract参数调优】
tesseract图片解析参数调优很重要
    语言包的选择
        英文 lang='eng'
        中文 lang='chi_sim'
    
    图片内容的特征
        --psm 13  单行内容模式
    
    OCR 引擎的选择
        --oem 0  最原始的tesseract引擎（为了支持白名单，必须选择这种引擎）
        
    白名单的配置
        数字白名单
        英文白名单


## 【训练tesseract】
https://github.com/tesseract-ocr/tesseract/wiki/TrainingTesseract-4.00  
      
----------------------------------------------------------
### 【安装python】
    2.7.15
    
### 【安装PIL（pytesser的使用需要PIL库的支持。）】
    pip install pillow
    
    http://effbot.org/imagingbook/introduction.htm
    
    
### 【安装pytessertact】

	>pip install pytesseract
	    Successfully installed pytesseract-0.2.2   
    

####【脚本】
使用pytesseract调用tesseract

》 test.py
	
	# -*- coding: utf-8 -*-  
	try:  
	    import Image  
	except ImportError:  
	    from PIL import Image
	import pytesseract  
	from sys import argv
	 
	filename = argv[1] 
	im = Image.open(filename)
	im.load()  
	#text = pytesseract.image_to_string(img, lang='chi_sim')  
	text = pytesseract.image_to_string(im, lang='eng')  
	print 'text=',text

 

  


