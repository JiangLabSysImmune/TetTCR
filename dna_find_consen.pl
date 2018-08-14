#based on the table (t.tab2), build consensus for each UMI
#
use strict;
use warnings;

my $file = $ARGV[0];

my %lens;  #use bc1.bc2.UMI as key
my %seqs;
my %scs;

#get dominate length
open (F,"<$file");
while (<F>) {
	chomp;
	my $line = $_;
	my @cts = split(/\t/,$line);
	my $key = $cts[0].".".$cts[1].".".$cts[2];
	my $len = length($cts[3]);
	if ($lens{$key}) {
		$lens{$key} = $lens{$key}.";".$len;
	}
	else {
		$lens{$key} = $len;
	}
	if ($seqs{$key}) {
		$seqs{$key} = $seqs{$key}.";".$cts[3];
	}
	else {
		$seqs{$key} = $cts[3];
	}
	if ($scs{$key}) {
		$scs{$key} = $scs{$key}.";".$cts[4];
	}
	else {
		$scs{$key} = $cts[4];
	}
}
close(F);


foreach my $key(keys %lens) {
	my $v = $lens{$key};
	my @cts = split(/\;/,$v);
	my %kk;
	foreach my $ct(@cts) {
		if ($kk{$ct}) {
			$kk{$ct} = $kk{$ct}+1;
		}
		else {
			$kk{$ct} = 1;
		}	
	}
	my @st = sort{$kk{$b}<=>$kk{$a}} keys %kk;
	my $mxn = $st[0];
	my $mxv = $kk{$mxn};

	#grab seqs/scores for consensus
	my %t_seq;
	my %t_sc;
	my $num = 0;
	my $k1 = $seqs{$key};
	my $k2 = $scs{$key};

	my @qq = split(/\;/,$k1);
	my @ss = split(/\;/,$k2);
	
	for (my $i = 0;$i <= $#qq;$i++) {
		if (length($qq[$i]) == $mxn) {
			$num++;
			$t_seq{$num} = $qq[$i];
			$t_sc{$num} = $ss[$i];
		}		
	}

	my $size = keys %t_seq;
	my $cons_name;
	if ($size == 1) {
		$cons_name = $key."_0_1";
	}
	else {
		$cons_name = $key."_1_".$size;
	}
	my $cons_seq = &consens(\%t_seq,\%t_sc);
	print ">$cons_name\n$cons_seq\n";
}

sub consens {
	my %gp_seq = %{$_[0]};
	my %gp_sco = %{$_[0]};
	
	my $sc_all = 100;

	my $cons = "";
	if (keys %gp_seq == 1) {
		my @key = keys %gp_seq;
		$cons = $gp_seq{$key[0]};
	}
	elsif (keys %gp_seq > 1) {
		my @key = keys %gp_seq;
		my $tmp = $gp_seq{$key[0]};
		for (my $i = 0;$i < length($tmp);$i++) {
			my %sub;
			$sub{"A"} = 0;
			$sub{"G"} = 0;
			$sub{"C"} = 0;
			$sub{"T"} = 0;
			$sub{"N"} = 0;

			foreach my $name(keys %gp_seq) {
				my $sub_seq = substr($gp_seq{$name},$i,1);
				my $sub_sco = substr($gp_sco{$name},$i,1);
				my $wgh = ord($sub_sco)/$sc_all;
				
				$sub{$sub_seq} = $sub{$sub_seq}+$wgh;
			}
			my $sub_cons = &find_max(\%sub);
			$cons = $cons.$sub_cons;
		}
	}
	return $cons;

}

sub find_max{
	#print "mmmmmm".$_[0];
	my %sub = %{$_[0]};

	#foreach my $kk(sort keys %sub) {
	#	print "$kk\t$sub{$kk}\n";
	#}


	my $max_char = "A";
	my $max = $sub{$max_char};

	foreach my $kk(keys %sub) {
		if ($sub{$kk} > $max) {
			$max = $sub{$kk};
			$max_char = $kk;
		}
	}
	return $max_char;
}



