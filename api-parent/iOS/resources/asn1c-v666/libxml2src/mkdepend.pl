# generate dependency rules file (c_rules.mk) 
# usage: mkdepend [-v]

$g_debug = 0;
$g_cfile = 0;

$numargs = @ARGV;

if ($numargs >= 1 && $ARGV[$numargs - 1] eq "-v") {
    $g_debug = 1;
}

# loop through all cpp files in the source directory

if (-e "c_rules.mk") {
    `rm -f c_rules.mk~`;
    `mv c_rules.mk c_rules.mk~`;
}
open (OUTFILE, ">c_rules.mk") || die ("could not open c_rules.mk for output");
print OUTFILE "# C to object file compilation rules\n";

$cccmd = "\t\$(CC) \$(CFLAGS) \$(LIBXML2DEFS) -c -I\$(LIBXML2INC)";

&addRules ("../libxml2src/*.c", $cccmd);

# add compile command for last rule
if ($g_cfile) {
    print OUTFILE "$cccmd $g_cfile\n";
    $g_cfile = 0;
}

close (OUTFILE);


sub addRules {
    local $sources = shift;
    local $cccmd = shift;
    local $extline = 0;
    local @rules = (`gcc $sources -E -MM -I. -I../libxml2src/include`);

    foreach $line (@rules) {
        # if line starts with a non-whitespace character, it is the 
        # start of a new rule ..
        if ($line =~ /^\w/) {
            # change .o to $(OBJ)
            $line =~ s/\.o/\$(OBJ)/;

            # add compile command
            if ($g_cfile) {
                print OUTFILE "$cccmd $g_cfile\n";
            }
            # add a newline 
            print OUTFILE "\n";

            # get C source file from rule
            $line =~ /^\w+\$\(OBJ\)\:\s+([\w\.\/]+)/;
            $g_cfile = $1;
            $g_cfile =~ s/\.\.\/libxml2src/\$\(LIBXML2SRCDIR\)/g;
            $line = '$(OBJDIR)$(PS)' . $line;

            # it is possible that if target file name is long, source file 
            # name will be on next line..
            $extline = 1 if (!$g_cfile);
        }
        elsif ($extline) {
            $line =~ /^\s+([\w\.\/]+)/;
            $g_cfile = $1;
            $g_cfile =~ s/\.\.\/libxml2src/\$\(LIBXML2SRCDIR\)/g;
            $extline = 0;
        }

        # change source file paths to use macro names
        $line =~ s/\.\.\/libxml2src\/include/\$\(LIBXML2INC\)/g;
        $line =~ s/\.\.\/libxml2src/\$\(LIBXML2SRCDIR\)/g;

        print OUTFILE $line;
    }
}

