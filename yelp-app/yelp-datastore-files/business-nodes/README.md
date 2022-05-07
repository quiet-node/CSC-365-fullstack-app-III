### This folder contains node-{business_id}.csv

### Store each business node in a separate csv like this will help drastically increase looking up time 

### An example of the node-0.csv

```
0,4292,0.061475865989630944,0.0
0,2512,0.11718671086739484,0.3563483225498992
0,3690,0.14400949883919292,0.1889822365046136
0,1059,0.18974387971642565,0.0
```

```0,2512,0.11718671086739484,0.3563483225498992 (line 2)```

- First column (0) is the root/requested businessID
- Second column (2512) is the destination businessID
- Third column (0.11718671086739484) is the distance between two nodes (calculated by Haversine)
- Fourth column (0.3563483225498992) is the similarity rate between two nodes (calculated by Cosine Similarity)
