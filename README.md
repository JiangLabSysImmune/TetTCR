#for deconvolution of TetATCR cell barcode

To run the pipeline:
1. name the pairwised sequencing as $prefix.R1.fastq and $prefix.R2.fastq
2. use the command: 
	bash run.sh $prefix
3. output file:
	$prefix.sq.tab
	Columns are :
	1).$prefix
	2) and 3). cell barcode
	4).UMI
	5).UMI based consensus seqs
	6).associated #reads
