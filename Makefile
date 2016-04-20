.PHONY = run clean

# TODO targets: test dist

OUTFOLDER=bin

all:
	@if [ ! -d "$(OUTFOLDER)" ]; then mkdir $(OUTFOLDER); fi
	
	javac -d $(OUTFOLDER) -sourcepath src/ src/Driver.java
	
	@# Copy resource files so the compiled code can find them when executed
	@for file in resources/*; do \
		cp $${file} bin/$$(basename $${file}); \
	done

run:
	@if [ -z "$(ARGS)" ]; then \
		echo 'Pass the path to the map data as:\n\t make ARGS="<path>" run'; \
	else \
		java -cp $(OUTFOLDER) Driver $(ARGS); \
	fi

clean:
	rm -rf $(OUTFOLDER)