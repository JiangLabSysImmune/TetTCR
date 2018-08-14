#cut the FASTA sequences into certain length
#for 250->150, should cut the last several !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
my $orfile = $ARGV[0];
my $cut_to = $ARGV[1];

#my $div = $ARGV[2];

open (OR,"<$orfile");
my $cur_name;
while (<OR>) {
	chomp;
	my $line = $_;
	if ($line =~ /^\>/) {
		$cur_name = $line;
	}
	else {
		my $seq = $line;
		$seq = substr($seq,0,length($seq)-5);
		#print "kkk$seq\n";
		my $sub_seq = substr($seq,length($seq)-$cut_to);
		print "$cur_name\n$sub_seq\n";
	}
}
close(OR);
