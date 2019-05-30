# Midterm Review {.text-center #home}

***Date***: *2017-02-16*

### Table of contents

#. [Chapter 1. Background][]
#. [Chapter 2. Fundamentals of Digital Imaging][]

---

## Chapter 1. Background

[Analog vs. Digital, Digitization][]  
[Bits basic concepts][]  
[How bits represent information][]  

### Analog vs. Digital, Digitization {.tab}

* **Analog information:**
	+ Continuous information
	+ An infinite number of divisions exist between any two measurements.
<br />  
* **Digital data**
	+ Discrete
		- Examples:
			* Number of persons. 
			* Choices in multiple-choice questions.
<br />  
* Analog vs Digital
	+ Analog data must be converted into finite discrete digital data in order for the computer to handle.
<br />  
* **Digitization**
	+ To convert analog information into digital data that computer can handle.
	+ Steps:
		- Sampling
			+ Sampling rate: how ofte you take a data
				- High: 
					+ pro: Can capture more details
					+ cons: produce more data, then take longer to read, to process and consume more space
				- Low:
					+ pro: Less data, shorter time to read, less space.
					+ cons: May miss details
		- Quantization
			+ Need to set <u>bit depth</u>: Refers to the number of allowable levels you map the values to.
<br />  
* **Digitizing media**:
	+ Images
		- Sampling rate: 
			+ Image resolution
			+ number of pixels
		- Bit depth:
			+ Number of allowable colors in an image.
	+ Video
		- Sampling rate:
			+ Number of pixels in the video
			+ frame rate
		- Bit depth:
			+ Number of allowable colors.
	+ Digital audio
		- Sampling rate:
			+ it limits how high the pith of the audio can be captured
		- Bit detph:
			+ Number of allowable levels of amplitude.
<br />  

### Bits basic concepts {.tab}

**Bits**

* Data is stored and represented in binary digits, called bits.
* A bit has two possible values, 0 or 1.
* `2 ^n = Number of possible values`. *n: number of bits*.
* 1 byte = 8bits.
* Prefixes:

<div class="bootstrap_table">
|Prefix Name | Abbreviation | Size                       |
|------------|:------------:|----------------------------|
| Kilo       | K            | `2 ^10 = 1024`             |
| Mega       | M            | `2 ^20 = 2048`             |
| Giga       | G            | `2 ^30 = 1073741824`       |
| Tera       | T            | `2 ^40 = 1099511627776`    |
| Peta       | P            | `2 ^50 = 1125899906842624` |
</div>

* **encoding**: 
* **Decoding**: Receiver needs to know how to interpret the signals.

### How bits represent information {.tab}

**Letters**

* **ASCII**:
	+ Stands for American Standard Code for Information Interchange.
	+ Encoding standard for text characters, including the 26-letter English alphabets and symbols in computer programs.
	+ Each character uses 8 bits.
<br />  
* **Unicode**:
	+ Can represent a large repertoire of multilingual characters.
	+ Use more than 8 bits to encode text character.
	+ Multilingual character sets are larger than the ASCII set.

**Images**

* **Bitmap**
	+ Digital photos.
	+ Color value of each pixel encoded into bits.
<br />  
* **Vector graphics**:
	+ Ex: graphics created in flash
	+ Coordinates of anchor points encoded into bits.
	+ Tangent of each anchor points encoded into bits.

**Sound**

* **Sampled audio**: Amplitude for each sample encoded into bits. For CD quality audio, it has 44,100 samples per second.
* **MIDI music**
	+ Each musical instrument has an ID which can be encoded into bits.
	+ Each musical note has an ID which can be encoded into bits.

**File sizes**

* Digital media files (image, sound, and specially video files) can be very large.
* Disadvantages of large file size
	+ Take longer to copy.
	+ Take longer to send over the internet.
	+ Take longer to process
* Strategies to reduce file size
	+ Reduce sampling rate.
	+ Reduce the bit depth.
	+ Apply file compression.
		- Two categories:
			+ <u>Lossy compression</u>: Some data will be lost and cannot be recovered
			+ <u>Lossless compression</u>:
		- Trade-offs
			+ Data will be lost when you apply these strategies: 
				- Reduce sampling rate
				- Reduce bit depth
				- Apply lossy compression

---

[[[]{.glyphicon .glyphicon-arrow-up} Back to top]{.btn .btn-default .pull-right}](#home)

## Chapter 2. Fundamentals of Digital Imaging

[Digitizing Images][]  
[Bitmapped Images vs. Vector Graphics][]  
[File size and File compression][]  

### Digitizing Images {.tab}

**Pixels**

* Each peg hole on the pegboard is a sample point.
* The sample points are discrete.
* Each of these discrete sample points is called *picture element* or *pixel* for short.

**Sampling Rate**

* Refers to how frequent you take a sample.
* Refers to how close neighboring samples are in a 2-D image plane.
* <u>Resolution</u> increasing the sampling rate is equivalent to increasing the image resolution.

**High resolution**

* The file size of the digitized image is larger.
* More detail from the original scence.

**Quantization**

* Quantizing the sampled image involves mapping the color of each pixel to a discrete and precise value.
* Consequences
	+ Reduce the number of allowed colors in the image.
	+ Different colors from the original may bemapped to the same color on the palette.
	+ Causes the loss of the image fidelity and details.
	+ <u>Bit Depth</u>: The most common bit depth is 24. A 24-bit image allows `2 ^24 = 16777216` colors.

### Bitmapped Images vs. Vector Graphics {.tab}

**Bitmapped images**

* Examples:
	+ Web images
		- JPEG
		- PNG
		- GIF
	+ Adobe Photoshop images
+ Bitmap images are resolution dependent; each image has a fixed resolution.
+ <u>Bitmap</u>: 1 bit per pixel.
+ <u>Pixmap</u>: Each pixel has a color value that uses more than 1 bit.

**Vector Graphics**

* Examples:
	- Adobe Flash
	- Adobe Illustrator
* Characteristics
	- Generated mathematically. i.e. instructions not pixel-based
	- Resolution independent.

**Bitmap vs vector**

* Bitmap takes more storage space.
* Vector take more computation to display.
* Vector is resolution independent.
* <u>RASTERING</u>: Converts vector graphics into pixel-based images.

### File size and File compression {.tab}

**Compression**

* Without compression, image files would take up unreasonable amount of disk space.
* Strategies
	+ Reducing the pixel dimensions
	+ Lowering the bit depth (Color depth)
	+ Compress the file
* File compression
	+ To reduce the size of a file by squeezing the same information into fewer bits.
* Lossless compression method
	+ TIFF, PNG, PSD
	+ GIF limits number of colors to 256
* Lossy compression method
	+ JPEG
