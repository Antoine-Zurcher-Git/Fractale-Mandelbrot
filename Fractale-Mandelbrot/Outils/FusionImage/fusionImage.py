
from PIL import Image, ImageFilter

nG = 7
im = []
nom = "-2,2,1,1,1,1,0,1,1,1,-76,-16,39,,8,32,0,C0-0,0,0,0,C1-255,255,255,1,C2-20,30,170,0,C3-241,254,25,0,C4-255,255,255,0r-.png"
for i in range(nG*nG):
    im.append(Image.open("49/"+str(i+1)+nom))
    
imgSize = im[0].size

new_im = Image.new('RGB', (nG*imgSize[0],nG*imgSize[1]), (250,250,250))

for y in range(nG):
    for x in range(nG):
        new_im.paste(im[x+y*nG],(imgSize[0]*x,imgSize[1]*y))

new_im.save("Fractale-resultat"+nom, "PNG")

#Read image
# im = Image.open( 'image.png' )
# Display image
# im.show()

#Applying a filter to the image
# im_sharp = im.filter( ImageFilter.SHARPEN )
# #Saving the filtered image to a new file
# im_sharp.save( 'image_sharpened.jpg', 'JPEG' )

#Splitting the image into its respective bands, i.e. Red, Green,
#and Blue for RGB
# r,g,b = im_sharp.split()

#Viewing EXIF data embedded in image
# exif_data = im._getexif()
# exif_data