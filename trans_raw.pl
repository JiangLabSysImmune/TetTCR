#From raw reads, extract FASTA sequences and score information
#
use strict;
use warnings;

my $ind = $ARGV[0];

my $raw1 = $ind.".R1.fastq";
my $raw2 = $ind.".R2.fastq";

my $out1 = $ind.".r1.fasta";
my $out2 = $ind.".r2.fasta";
my $outs1 = $ind.".r1.score";
my $outs2 = $ind.".r2.score";


#based on raw2;
my %seq2;
my %seq1;
my %sc2;
my %sc1;

my @ors;
print "reading 1\n";
open (RT,"<$raw2");
my $cur_name;
my $cur_num;
my $cur_name_num;

while (<RT>) {
	chomp;
	$cur_num++;
	my $line = $_;
	if ($line =~ /\@M/) {
		#print "$_\n";
		my @cts = split(/\s/,$line);
		$cur_name = $cts[0];
		$cur_name_num = $cur_num;
		push (@ors,$cur_name);
	}
	elsif ($cur_num == $cur_name_num+1) {
		$seq2{$cur_name} = $line;
		#print "$line\n";
	}
	elsif ($cur_num == $cur_name_num+3) {
		$sc2{$cur_name} = $line;
		#print "$line\n";
	}
}
close(RT);

print "reading2\n";
open (RO,"<$raw1");
my $cur_name1;
my $cur_num1;
my $cur_name_num1;
while (<RO>) {
        chomp;
        $cur_num1++;
        my $line = $_;
        if ($line =~ /\@M/) {
		my @cts = split(/\s/,$line);
                $cur_name1 = $cts[0];
                $cur_name_num1 = $cur_num1;
        }
        elsif ($cur_num1 == $cur_name_num1+1) {
                $seq1{$cur_name1} = $line;
        }
        elsif ($cur_num1 == $cur_name_num1+3) {
                $sc1{$cur_name1} = $line;
        }
}
close(RO);

my %sp_seq2;
my %sp_sc2;
my %sp_seq1;
my %sp_sc1;
#my $out1 = "Jill".$jill_num.".r1.fasta";
#my $out2 = "Jill".$jill_num.".r2.fasta";
#my $outs1 = "Jill".$jill_num.".r1.score";
#my $outs2 = "Jill".$jill_num."r2.score";

open (OUT1,">$out1");
open (OUT2,">$out2");
open (OUTS1,">$outs1");
open (OUTS2,">$outs2");

foreach my $name(@ors) {
	print OUT1 ">$name\n$seq1{$name}\n";
	print OUT2 ">$name\n$seq2{$name}\n";
	print OUTS1 "|$name\n$sc1{$name}\n";
	print OUTS2 "|$name\n$sc2{$name}\n";	
}


close(OUT1);
close(OUT2);
close(OUTS1);
close(OUTS2);
