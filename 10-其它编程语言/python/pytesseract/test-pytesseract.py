# -*- coding: utf-8 -*- 
try:  
    import Image  
except ImportError:  
    from PIL import Image
import pytesseract  
from sys import argv
 
filename = argv[1] 
whitelist = argv[2]

im = Image.open(filename)
im.load()  
im.show()
#im = im.convert('L') 

text = ''

if whitelist == 'my-digits':
    print 'digits'
    #text = pytesseract.image_to_string(im, lang='chi_sim', config='--psm 13 --oem 0 my-digits')  
    text = pytesseract.image_to_string(im, lang='eng', config='--psm 13 --oem 0 my-digits')  
else:
    print 'chars'
    text = pytesseract.image_to_string(im, lang='eng', config='--psm 13 --oem 0 my-chars')

print(text)

