#!/bin/bash

RESOURCE_DIR="resources"

svg_files=$(find $RESOURCE_DIR -name "*.svg")

function parse_layer_name {
    svg=$1
    layer_id=$2

    echo $(cat $svg \
     | grep $layer -A 1 \
     | sed -n s/inkscape:label=\"//p \
     | sed s/\"// \
     | sed s/\>//
     )
}

function export_image {
    svg=$1
    layer_id=$2

    layer_name=$(parse_layer_name $svg $layer_id)
    svg_name=$(basename $1 .svg)
    filename=${svg_name}_${layer_name}.png
    
    inkscape $svg \
        --without-gui \
        --export-id-only \
        --export-area-page \
        --export-dpi=48 \
        --export-id=$layer_id \
        --export-png=$RESOURCE_DIR/img/$filename
}

for svg in $svg_files; do
    layers=$(inkscape --query-all $svg | grep layer | cut -f 1 -d ,)
    for layer in $layers; do
        export_image $svg $layer
    done
done
