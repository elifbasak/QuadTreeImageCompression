# QuadTreeImageCompression
Elif Ba˛ak YILDIRIM
161101032

- Dogru Kullan˝mlar -

Java Main -c -o output -i kira.ppm

Java Main -e -o output -i kira.ppm

-----------------------------
Eger test resiminde sonsuz dˆng¸ye girerse , test etmek iÁin bu sat˝r˝ kullanabilirsiniz.
( threshold deeri 5 iken olu˛an resimi verir. ( Yorum sat˝r˝ olarak mainde duruyor) ).

        QuadTree tree = new QuadTree(5,inputFileName , outputFileName+"TEST.ppm");

-----------------------------
Compress ederken Threshold deerini ( double denenenThreshold ) -20 den ba˛latt˝m.
Uygun compress deerini bulmak iÁin threshold deerini artt˝r˝p/azalt˝yor.

------------------------------

-e komutu ile Áa˝r˝l˝rsa :
	*-EDGE.ppm dosyas˝nda edge detection hali , 
	*-NEGATIVE.ppm dosyas˝nda resimin negatif hali ,
	*-GRAYSCALE.ppm dosyas˝nda resimin gri filtreli hali sorunsuz ˛ekilde olu˛uyor.

(*EDGE.ppm dosyas˝n˝n threshold deeri : 20 ). 

kira.ppm iÁin elde ettiim veriler:

«al˝˛ma s¸resi 1 dakika 13 saniye.

	threshold	|	compression
+-----------------------+-----------------------------------------+
|	0		|	62.653		---> output-8.ppm |
+-----------------------+-----------------------------------------+
|	-1		|	49		---> output-7.ppm |
+-----------------------+-----------------------------------------+
|	5		|	21.27		---> output-6.ppm |
+-----------------------+-----------------------------------------+
|	14		|	8.77		---> output-5.ppm |
+-----------------------+-----------------------------------------+
|	38		|	3.92		---> output-4.pm  |
+-----------------------+-----------------------------------------+
|	83		|	1.13		---> output-3.ppm |
+-----------------------+-----------------------------------------+
|	107		|	0.4		---> output-2.ppm |
+-----------------------+-----------------------------------------+
|	119		|	0.22		---> output-1.ppm |
|_________________________________________________________________|



 
