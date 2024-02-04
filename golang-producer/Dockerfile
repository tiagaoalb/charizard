FROM golang:1.20.0-alpine

# Create directories
RUN mkdir input && \
    mkdir conciliation

# Copy binary golang
COPY binario binario

# Set entry point
ENTRYPOINT [ "./binario" ]