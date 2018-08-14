#formalize table 1 for later analysis
#--- based on the 3 alignment files
use strict;
use warnings;

my $file1 = "primer.algn.res";
my $file2 = "cons1.algn.res";
my $file3 = "cons2.algn.res";

my $seqf = $ARGV[0];  #ind.160.fasta
my $scf = $ARGV[1];   #ind.160.score

my $thr = 3;

my @algn1;
my @algn2;
my @algn3;
my @seqs;
my @scs;

open (F,"<$seqf");
while (<F>) {
	my $line = $_;
	chomp($line);
	if ($line =~ /^\>/) {
		my $seq = <F>;
		chomp ($seq);
		push (@seqs,$seq);
	}
}
close(F);

open (F,"<$scf");
while (<F>) {
	my $line = $_;
	chomp($line);
	if ($line =~ /^\|/) {
		my $seq = <F>;
		chomp ($seq);
		push (@scs,$seq);
	}
}
close(F);

open (F,"<$file1");
@algn1 = <F>;
chomp (@algn1);
close(F);

open (F,"<$file2");
@algn2 = <F>;
chomp (@algn2);
close(F);

open (F,"<$file3");
@algn3 = <F>;
chomp (@algn3);
close(F);

for (my $i = 0;$i <= $#algn1;$i++) {
	my $line1 = $algn1[$i];
	my $line2 = $algn2[$i];
	my $line3 = $algn3[$i];
	my $seq = $seqs[$i];
	my $score = $scs[$i];

	$line1 =~ /^\>([AGCTN]+)\.([AGCTN]+)\_/;
	my $bb1 = $1;
	my $bb2 = $2;

	my @cts1 = split(/\t/,$line1);
	my @cts2 = split(/\t/,$line2);
	my @cts3 = split(/\t/,$line3);

	if ($cts1[3] <= $thr and $cts2[3] <= $thr and $cts3[3] <= $thr) {
		#filter by primer pos
		my $bc1 = substr($seq,$cts1[1]-6,6);
		my $bc2 = substr($seq,$cts3[2]);
		if ($bc1 eq $bb1 and $bc2 eq $bb2) {
			#filter by UMI length
			if ($cts2[1]-$cts1[2] == 12) {
				my $umi = substr($seq,$cts1[2],$cts2[1]-$cts1[2]);
				my $dnaBC = substr($seq,$cts2[2],$cts3[1]-$cts2[2]);
				my $dnaSC = substr($score,$cts2[2],$cts3[1]-$cts2[2]);
				print "$bc1\t$bc2\t$umi\t$dnaBC\t$dnaSC\t$seq\n";
			}
		}
	}


}


