##########change all these files before start!!!###################
##########2. dna.bc.seq(lsit of all dna cell barcode seqs)#########

##########rename sequencing results as ($ind).R[12].fastq##########
#set parameters - from command line
ind=$1;  #prefix of filenames of FASTQ file, i.e. $ind.R[12].fastq

javac *.java
######STEP1: preparing reads for clustering on sequence similiarity
perl trans_raw.pl $ind                                        #translate FASTQ file into FASTA files, keep sequences and quality score in seperate files
perl group_into_barcode.pl $ind                               #identifile barcodes of each sequence(only on read 1), group sequences into barcode groups,for different experimental design, 								     #should change parameters in the script

##STEP1: preparing reads for clustering on sequence similiarity
#perl trans_raw.pl $ind                                        #translate FASTQ file into FASTA files, keep sequences and quality score in seperate files
perl dna_group_into_barcode.pl $ind                               #identifile barcodes of each sequence(only on read 1), group sequences into barcode groups,for different experimental design, 								     #should change parameters in the script
perl dna_cut_seq_len.pl $ind.dna250.fasta 130 >$ind.130.fasta        #cut read1 to be 150nt
perl dna_cut_sc_len.pl $ind.dna250.score 130 >$ind.130.score         #cut read1 quality score to be 150

java alignHam $ind.130.fasta cons.fw.primer >primer.algn.res
java alignHam $ind.130.fasta cons.seq1 >cons1.algn.res
java alignHam $ind.130.fasta cons.seq2 >cons2.algn.res

#formalize table 1 for later analysis
perl gen.tab1.pl $ind.130.fasta $ind.130.score >t.tab1

#filter table 1 with designed cell barcode ############dma.bc.seq should change accordingly#########
perl filt.with.dnabc.pl t.tab1 dna.bc.seq >t.tab2

perl tab.dna_find.consen.pl t.tab2 $ind >$ind.sq.tab


