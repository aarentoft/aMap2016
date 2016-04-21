.PHONY: all test clean

# TODO targets: dist

OUTFOLDER=bin
PRODUCTION_OUT=$(OUTFOLDER)/production
TEST_OUT=$(OUTFOLDER)/test

# Checks if a folder exists, and if it does not, it is created.
#
# Params:
# 	1 name of folder
create_folder = \
	if [ ! -d "$(1)" ]; then mkdir -p $(1); fi

all:
	@$(call create_folder, $(PRODUCTION_OUT))
	
	javac -d $(PRODUCTION_OUT) -sourcepath src/ src/Driver.java
	
	@# Copy resource files so the compiled code can find them when executed
	@for file in resources/*; do \
		cp $${file} $(PRODUCTION_OUT)/$$(basename $${file}); \
	done

run:
	@if [ -z "$(ARGS)" ]; then \
		echo 'Pass the path to the map data as:\n\t make ARGS="<path>" run'; \
	else \
		java -cp $(PRODUCTION_OUT) Driver $(ARGS); \
	fi

test:
	make
	@$(call create_folder, $(TEST_OUT))
	javac -d $(TEST_OUT)/ -cp src:third_party_lib/junit-4.12.jar -sourcepath src/:test/ test/TestSuite.java

	java -cp .:bin/test:$(JUNIT):$(HAMCREST):$(PRODUCTION_OUT) \
		-DtestDataFilePath="data/map-anholt.osm" \
		org.junit.runner.JUnitCore TestSuite

clean:
	rm -rf $(OUTFOLDER)