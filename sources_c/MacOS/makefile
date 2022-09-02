CC = g++
DEBUG = -g
PROFILE = -fprofile-instr-generate
CFLAGS = -m64 -Wall -W $(DEBUG) $(PROFILE)
LDFLAGS = -m64 $(PROFILE)
CALE = ./src/cod
TARGETS = client server cleaner data_hashing_utility run_expansion_generation run_configuration run_modul1 run_modul2 run_modul3 run_modul4 run_modul3_stitch hash_result_testing

build: $(TARGETS)

###############################################################
client : $(CALE)/module/client.o $(CALE)/utilities/utils.o $(CALE)/utilities/structures_interval.o $(CALE)/utilities/structures_expanded.o $(CALE)/module/m1_hash_retrieval.o $(CALE)/utilities/structures_aliniere.o $(CALE)/module/m2_reuniune.o $(CALE)/module/m3_expand.o $(CALE)/module/m3_stitch.o $(CALE)/module/m4_pick.o $(CALE)/module/m4_smith-waterman.o $(CALE)/module/m5_compunere_rezultate.o $(CALE)/utilities/expansion_generation.o $(CALE)/utilities/configuration.o $(CALE)/utilities/gene_retrieval.o
	$(CC) $(LDFLAGS) -o $@ $^ 

$(CALE)/module/client.o : $(CALE)/module/client.cpp
	$(CC) $(CFLAGS) -c -o $@ $^

$(CALE)/module/m5_compunere_rezultate.o : $(CALE)/module/m5_compunere_rezultate.cpp
	$(CC) $(CFLAGS) -c -o $@ $^
	
#~~~~~~~#
run_modul1: $(CALE)/test/run_modul1.o $(CALE)/module/m1_hash_retrieval.o $(CALE)/utilities/utils.o
	$(CC) $(LDFLAGS) -o $@ $^

$(CALE)/test/run_modul1.o: $(CALE)/test/run_modul1.cpp
	$(CC) $(CFLAGS) -c -o $@ $^

$(CALE)/module/m1_hash_retrieval.o : $(CALE)/module/m1_hash_retrieval.cpp
	$(CC) $(CFLAGS) -c -o $@ $^

#~~~~~~~#
run_modul2: $(CALE)/test/run_modul2.o $(CALE)/utilities/structures_interval.o $(CALE)/module/m2_reuniune.o $(CALE)/utilities/utils.o $(CALE)/module/m1_hash_retrieval.cpp
	$(CC) $(LDFLAGS) -o $@ $^

$(CALE)/test/run_modul2.o: $(CALE)/test/run_modul2.cpp
	$(CC) $(CFLAGS) -c -o $@ $^

$(CALE)/module/m2_reuniune.o : $(CALE)/module/m2_reuniune.cpp
	$(CC) $(CFLAGS) -c -o $@ $^
	
#~~~~~~~#
run_modul3: $(CALE)/test/run_modul3.o $(CALE)/module/m3_expand.o $(CALE)/utilities/structures_expanded.o $(CALE)/utilities/utils.o $(CALE)/utilities/expansion_generation.o $(CALE)/utilities/configuration.o
	$(CC) $(LDFLAGS) -o $@ $^

$(CALE)/test/run_modul3.o: $(CALE)/test/run_modul3.cpp
	$(CC) $(CFLAGS) -c -o $@ $^

$(CALE)/module/m3_expand.o : $(CALE)/module/m3_expand.cpp
	$(CC) $(CFLAGS) -c -o $@ $^
	
#~~~~~~~#
run_modul3_stitch: $(CALE)/test/run_modul3_stitch.o $(CALE)/module/m3_stitch.o $(CALE)/utilities/structures_expanded.o $(CALE)/utilities/utils.o
	$(CC) $(LDFLAGS) -o $@ $^

$(CALE)/test/run_modul3_stitch.o: $(CALE)/test/run_modul3_stitch.cpp
	$(CC) $(CFLAGS) -c -o $@ $^

$(CALE)/module/m3_stitch.o : $(CALE)/module/m3_stitch.cpp
	$(CC) $(CFLAGS) -c -o $@ $^
	
#~~~~~~~#
run_modul4: $(CALE)/test/run_modul4.o $(CALE)/module/m4_smith-waterman.o $(CALE)/utilities/utils.o $(CALE)/utilities/structures_aliniere.o
	$(CC) $(LDFLAGS) -o $@ $^

$(CALE)/test/run_modul4.o: $(CALE)/test/run_modul4.cpp
	$(CC) $(CFLAGS) -c -o $@ $^

$(CALE)/module/m4_smith-waterman.o : $(CALE)/module/m4_smith-waterman.cpp
	$(CC) $(CFLAGS) -c -o $@ $^

$(CALE)/module/m4_pick.o : $(CALE)/module/m4_pick.cpp
	$(CC) $(CFLAGS) -c -o $@ $^

$(CALE)/utilities/structures_aliniere.o: $(CALE)/utilities/structures_aliniere.cpp
	$(CC) $(CFLAGS) -c -o $@ $^
	
###############################################################
cleaner : $(CALE)/utilities/cleaner.o
	$(CC) $(LDFLAGS) -o $@ $^ 

$(CALE)/utilities/cleaner.o : $(CALE)/utilities/cleaner.cpp
	$(CC) $(CFLAGS) -c -o $@ $^

#~~~~~~~#
server : $(CALE)/utilities/server.o $(CALE)/utilities/gene_retrieval.o
	$(CC) $(LDFLAGS) -o $@ $^ 

$(CALE)/utilities/server.o : $(CALE)/utilities/server.cpp 
	$(CC) $(CFLAGS) -c -o $@ $^

#~~~~~~~#
data_hashing_utility: $(CALE)/utilities/data_hashing_utility.o $(CALE)/utilities/utils.o
	$(CC) $(LDFLAGS) -o $@ $^ 

$(CALE)/utilities/data_hashing_utility.o : $(CALE)/utilities/data_hashing_utility.cpp
	$(CC) $(CFLAGS) -c -o $@ $^

$(CALE)/utilities/utils.o: $(CALE)/utilities/utils.cpp
	$(CC) $(CFLAGS) -c -o $@ $^

$(CALE)/utilities/structures_interval.o: $(CALE)/utilities/structures_interval.cpp
	$(CC) $(CFLAGS) -c -o $@ $^
	
$(CALE)/utilities/structures_expanded.o: $(CALE)/utilities/structures_expanded.cpp
	$(CC) $(CFLAGS) -c -o $@ $^

#~~~~~~~#
	
hash_result_testing: $(CALE)/test/hash_result_testing.o $(CALE)/utilities/utils.o
	$(CC) $(LDFLAGS) -o $@ $^

$(CALE)/test/hash_result_testing.o: $(CALE)/test/hash_result_testing.cpp
	$(CC) $(CFLAGS) -c -o $@ $^
	
#~~~~~~~#
run_expansion_generation: $(CALE)/test/run_expansion_generation.o $(CALE)/utilities/expansion_generation.o $(CALE)/utilities/utils.o $(CALE)/utilities/configuration.o
	$(CC) $(LDFLAGS) -o $@ $^

$(CALE)/test/run_expansion_generation.o: $(CALE)/test/run_expansion_generation.cpp
	$(CC) $(CFLAGS) -c -o $@ $^

$(CALE)/utilities/expansion_generation.o : $(CALE)/utilities/expansion_generation.cpp
	$(CC) $(CFLAGS) -c -o $@ $^
#~~~~~~~#
run_configuration: $(CALE)/test/run_configuration.o $(CALE)/utilities/configuration.o
	$(CC) $(LDFLAGS) -o $@ $^

$(CALE)/test/run_configuration.o: $(CALE)/test/run_configuration.cpp
	$(CC) $(CFLAGS) -c -o $@ $^

$(CALE)/utilities/configuration.o : $(CALE)/utilities/configuration.cpp
	$(CC) $(CFLAGS) -c -o $@ $^
#~~~~~~~#
$(CALE)/utilities/gene_retrieval.o : $(CALE)/utilities/gene_retrieval.cpp
	$(CC) $(CFLAGS) -c -o $@ $^

###############################################################

clean:
	rm -f $(OBJ) $(TARGETS)
	rm -f `find . -name  '*.o'`;
	rm -f `find . -name  '*~'`;
