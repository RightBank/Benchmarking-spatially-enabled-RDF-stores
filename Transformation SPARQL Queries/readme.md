# Transformation Queries

These SPARQL queries were designed to transform the geospatial RDF data dwonlaoded from Integrated Carbon Observation System (ICOS) Carbon Portal (CP). The intent of the queries is to transform the data from Carbon Portal ontology to GeoSPARQL. There are three different queries each deals a separate type of geometry i.e. Polygons, Linestrings and Points. Each query returns the data in graph format.

The transformed data is also included in the `CPMeta.rdf` file available in the same repository. This file can be used as input to the umbrella test prorgams with mode=general.