
DOCKER_IMAGE  = "tp-jan-22"
TEX = $(wildcard src/*/main.tex)
RPLOTS = $(TEX:main.tex=Rplots.pdf)
BIN = bin

CONTAINER := $(DOCKER_IMAGE)-$(notdir $(shell mktemp))

# RUN THESE TARGETS INSIDE OF THE DOCKER IMAGE OR INSTALL THE REQUIRED PACKAGES

# Compile the project
all: ${BIN}/out.pdf

${BIN}/out.pdf: src/main.pdf
	cp src/main.pdf ${BIN}/out.pdf

# Build the main pdf
src/main.pdf: src/main.tex ${TEX}
	mkdir -p ${BIN} # Create output dir
	cd src && latexmk -pdf -interaction=nonstopmode main.tex # Compile main pdf
# Build the plot pdf


# Clean built files
clean:
	-rm -r ${BIN}
	-cd src && rm *.aux */*.aux *.fdb_latexmk *.fls *.log *.toc *.out *.pdf */Rplots.pdf


# RUN THESE TARGETS OUTSIDE OF THE DOCKER IMAGE

# Compile the project in docker
docker: docker
	docker build -t ${DOCKER_IMAGE} . # Build docker image
	docker run --name $(CONTAINER) -it ${DOCKER_IMAGE} $(MAKE) all # Compile the project in a container
	mkdir -p ${BIN} # Create the output dir
	docker cp $(CONTAINER):/opt/tp/${BIN}/out.pdf ${BIN}/out.pdf # Extract output from container
	docker container rm $(CONTAINER) # Cleanup


.PHONY: all clean docker
