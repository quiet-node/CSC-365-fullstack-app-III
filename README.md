# CSC-365 fullstack app 3.0

## Overview
  - This project is the extention of the [CSC-365-fullstack-app 1.0](https://github.com/lgad31vn/CSC-365-fullstack-app) and [CSC-365-fullstack-app 2.0](https://github.com/lgad31vn/CSC-365-fullstack-app-II)
  
  - Version 1.0 is a full-stack web application that works like a *search engine* which will find the similar and relevant businesses (in a json dataset from Yelp) based on user's inputs using Cosine Similarity Metric. 
  
  - Version 2.0 allows users to fetch 5-10 clusters randomly from the Spring Boot server. It then feed the clusters to React+D3.js (Data-driven document) in the FE to form up a hierarchical tree.

  - This new extended version 3.0 applies graph theory to form up graphs based on geographical distances, and utilize Dijkstra's alogorithm to find the shortest path (weighted by similarity rate) between two chosen nodes in the graph.
  
## Project Showcase

### React D3 Graph showcase
https://user-images.githubusercontent.com/66233296/168176621-e0f63324-40eb-44f0-b209-ca20fc73fac1.mp4


### Dijkstra Shortest Path showcase
https://user-images.githubusercontent.com/66233296/168176857-7b2f864e-3db9-4402-8ddd-95fb7983dbb2.mp4



## Technologies:
### Backend -- Engine
  - Java Springboot
  - Customized B-Tree
  - Java NIO/IO
  - Clustering using K-Means
  - Customized HashTable
  - Cosine Similarity
  - MySql Database / Docker image
  - Haversine formula
  - Graph theory (Dijkstra's algorithm, Union-Find Disjoint Set, Weighted Graph)


### Frontend -- client
  - Vite/React--TypeScript
  - React D3 Graph
  - Tailwindcss
  - Material-UI

## Resources
### Version 3.0
  - [Union-find for disjoin set -- course's notes](https://docs.google.com/document/d/1vL7tjxZzut8Cl7L2KYfp9S8DlFDHnWCG4Gwekg8vRWQ/edit#heading=h.m17n12tmqn83)
  - [Union-find for disjoint set -- hackerearth](https://www.hackerearth.com/practice/notes/disjoint-set-union-union-find/)
  - [Union-find for disjoint set -- cp-algo](https://cp-algorithms.com/data_structures/disjoint_set_union.html)
  - [Union-find for disjoint set -- geeksforgeeks](https://www.geeksforgeeks.org/disjoint-set-data-structures/)
  - [Kraskal's Algorithm -- cp-algo](https://cp-algorithms.com/graph/mst_kruskal.html)
  - [MST and Kraskal's Algorithm -- cp-algo](https://cp-algorithms.com/graph/mst_kruskal_with_dsu.html)
  - [Haversine implementation -- geeksforgeeks](https://www.geeksforgeeks.org/haversine-formula-to-find-distance-between-two-points-on-a-sphere/)
  - [Dijkstra's algorithm](https://www.baeldung.com/java-dijkstra)
  - [React D3 Graph](https://github.com/danielcaldas/react-d3-graph)

### Version 2.0
  - [Yelp Dataset -- yelp](https://www.yelp.com/dataset)
  - [Btree -- Geeksforgeeks](https://www.geeksforgeeks.org/insert-operation-in-b-tree/) 
  - [K-Means -- Baeldung](https://www.baeldung.com/java-k-means-clustering-algorithm)
  - [D3 -- Data-driven Document](https://observablehq.com/@d3/d3-hierarchy?collection=@d3/d3-hierarchy)

### Version 1.0
  - [Yelp Dataset](https://www.yelp.com/dataset)
  - [Cosine Similarity](https://www.machinelearningplus.com/nlp/cosine-similarity/)
