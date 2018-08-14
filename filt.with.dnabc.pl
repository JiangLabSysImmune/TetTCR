#filter t.tab1 with dna barcode
#
use strict;
use warnings;

my $file1 = $ARGV[0];  #t.tab1
my $file2 = $ARGV[1];  #bc file
my %bcs;
open (F,"<$file2");
while (<F>) {
	chomp;
	my $line = $_;
	if (length($line) >1) {
		my @cts = split(/\t/,$line);
		$cts[0] =~ s/\s//g;
		$cts[1] =~ s/\s//g;
		my $bc = $cts[0].".".$cts[1];
		$bcs{$bc} = 1;
	}
}
close(F);

open (F,"<$file1");
while (<F>) {
	chomp;
	my $line = $_;
	my @cts = split(/\t/,$line);
	my $bc = $cts[0].".".$cts[1];
	if ($bcs{$bc}) {
		print "$line\n";;
	}
}
close(F);
