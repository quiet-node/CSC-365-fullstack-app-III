# CSC-365 fullstack app 2.0


## Overview
  - This project is the extention of the [CSC-365-fullstack-app](https://github.com/lgad31vn/CSC-365-fullstack-app).
  
  - This is a full-stack web application that works like a *search engine* which will find the similar and relevant businesses (in a json dataset from Yelp) based on user's inputs using Cosine Similarity Metric. 
  
  - This new extended version allow users to fetch 5-10 clusters randomly from the Spring Boot server. It then feed the clusters to React+D3.js (Data-driven document) in the FE to form up a hierarchical tree 
  

## Project Showcase
https://user-images.githubusercontent.com/66233296/161447005-ec8382ae-827d-4b8d-a350-f8e0dae2cf9c.mov



## Technologies:
### Backend -- Engine
  - Java Springboot
  - Customized B-Tree
  - Java NIO/IO
  - Clustering using K-Means
  - Customized HashTable
  - Cosine Similarity
  - MySql Database / Docker image


### Frontend -- client
  - Vite/React--TypeScript
  - D3.js (Data-driven document)
  - Tailwindcss
  - Material-UI

### Resources
  - [Yelp Dataset](https://www.yelp.com/dataset)
  - [Btree - Geeksforgeeks](https://www.geeksforgeeks.org/insert-operation-in-b-tree/) 
  - [K-Means - Baeldung](https://www.baeldung.com/java-k-means-clustering-algorithm)
  - [D3 - Data-driven Document](https://observablehq.com/@d3/d3-hierarchy?collection=@d3/d3-hierarchy)
  
