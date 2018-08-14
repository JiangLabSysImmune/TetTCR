#given the FASTA file from raw reads, group them into barcodes
#
use strict;
use warnings;

my $ind = $ARGV[0];

my $ff1 = $ind.".r1.fasta";
my $ff2 = $ind.".r2.fasta";

my $sf1 = $ind.".r1.score";

my $idf = $ind.".index";
my $out1 = $ind.".250.fasta";
my $out2 = $ind.".250.score";

open (ID,">$idf");
open (OUT1,">$out1");
open (OUT2,">$out2");

my %all_info;
my %sc_info;

print "reading file 1\n";
my $cur_name;
my %ref_info;
open (FFT,"<$ff1");
while (<FFT>) {
	chomp;
	my $line = $_;
	if ($line =~ /^\>(.+)/) {
		$cur_name = $1;
	}
	elsif ($line =~ /^[AGCTN]/) {
		$ref_info{$cur_name} = $line;
	}
}
close(FFT);

print "reading score 1\n";
my $cur_names;
my %ref_infos;
open (SFT,"<$sf1");
while (<SFT>) {
        chomp;
        my $line = $_;
        if ($line =~ /^\|(.+)/) {
                $cur_names = $1;
        }
        else {
                $ref_infos{$cur_names} = $line;
        }
}
close(SFT);


print "reading file 1\n";
open (FFO,"<$ff1");
my %cur_info;
my $tmp_name;
while (<FFO>) {
	chomp;
	my $line = $_;
	if ($line =~ /^\>(.+)/) {
		$tmp_name = $1;
	}
	elsif ($line =~ /^[AGCTN]/) {
		my $rd = $line;
		my $seq = $ref_info{$tmp_name};
		my $seq2 = &rev($seq);
		
		my $len = length($seq2);
		my $bc = substr($seq2,$len-6,6); #get the barcode
		
                if ($cur_info{$bc}) {
                        $cur_info{$bc}++;
                }
                else {
                        $cur_info{$bc} = 1;
                }
                my $i = $cur_info{$bc};

                my $tp = $bc."_".$i;
			
		$all_info{$tp} = $seq;
		$sc_info{$tp} = reverse($ref_infos{$tmp_name});
		#print ">$tp\n$tmp_name\n$seq2\n$seq\n";
		print ID "$tmp_name\t$tp\n";
		print OUT1 ">$tp\n$seq2\n";
		print OUT2 "|$tp\n$sc_info{$tp}\n";
	} 
}
close(FFO);
close(ID);




sub rev{
	my $seq = $_[0];
	$seq =~ s/A/M/g;
	$seq =~ s/T/A/g;
	$seq =~ s/M/T/g;
	
	$seq =~ s/G/M/g;
	$seq =~ s/C/G/g;
	$seq =~ s/M/C/g;
	
	$seq = reverse($seq);
	return ($seq);
	
	
}
