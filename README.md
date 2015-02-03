# GS.avi
beta code

GS.avi is a gestural instrument that generates continuous spatial visualizations and music from the input of a performer. The features extracted form a performer’s gesture defines the color, position, form and orientation of a 3-dimensional Delaunay mesh. The music, composed using granular synthesis, is generated from features extracted from the mesh. The project was created using Processing and MAX/MSP. OSC is used to communicate between the two.

# Usage

NOTE: This MAX/MSP patch is a simulation to test that the Processing sketch generates visuals. There is no sound. 

# 1: To use this patch:

A. To import the patch into MAX/MSP, Copy and Paste the enclosed compressed text into the patcher window OR Copy and from the MAX/MSP menu, choose File > New From Clipboard. 

B. Unlock the MAX/MSP patch by selecting the lock icon on the bottom left of the patcher window by clicking once. The lock should appear open. This action unlocks all of the objects in the patcher window. 

C. Click all of the white boxes at the top left of each object cluster for the objects “/duration”, “/phase”, “/angle”, “/scaler”, “/magnitude”, and “/gesture” to enable the toggle switches. An “X” should appear in all of the boxes. Numbers should start to generate for each object.  

D. Lock the patcher by selecting the lock icon on the lower left of the patcher. The lock should appear locked.

J. Start the processing sketch. 

K. Use the trackpad or mouse to begin drawing the mesh.

<pre><code>----------begin_max5_patcher----------1729.3oc4bssiiZDD84weEHTdzgz2uj2x2wpnULFhGVYCV.dyrY09uG.eglYmKkGnZyrddXjbaiopCm5bptoweewcg2W7XZUXveF7of6t66Kt6ttgZG3tiu9tvswOtZSbU2GKrZSVRZY3xCuUw95Mo00eaW5guivvf+93asKtd0CY4q+bY5p5CuKkxjQVt0JnKCXBSDkx4L8x.IOhzNRD47gmueaVdy2c2YkdbvrjtXn39u76FV34STY71z5zxOmlGe+ltHgz+sbHDG70TTlklWGWmUj6NbU82NbzggsC7iEKZ+2Rf.Syo59WCXVFDdeb95d.5eJxqyah7ti9uJyh2DBE5L1HKwZ07yPG+sft1yVU1+0c1nsGyOin7KGQYSBzMJNEyRhHBBgaGOmhbawoF.cFcDgYa9aR4TzqEmJO8eaBfSm85zG6R5vx37jhsAxWDSyxqe+vo5LbRkpHgxZMpkAJ4qCmLvvoVE9pjvwAY0EqW2b3fPlmM+Eh972nhrzl7uQhhQ5xexETIp0u+JQT3MMgPYQ.mPHuH9LpJQGniQYQ71yDaYfl1AcrKh5vdVpiIz+n1wpMEJUaNPFuAgj5FHSOoUa1qGjQIXfYtMQ3pPo4SElol0JTDMqO+OpPoLuKEJ07TghIQRgxE55TnZ5UvzTtoYSkBkx7qV4lKl4JQMckaZ10Dyv.zzJcDuQVxJGJQYHSlDEYFKQoYz9z+nBk8cpPQmmJT3HP4BbtcPcrcfoPeB0ZswLGXsg0m6iaJvJ9M0TfGfbskaDNWRmxY.qDyyY.ij5sSUnqi2DpdKudNdbEFXlTahXZkRyvxwSJlwNdRNqO8G2pFHkyRGOJNNdt.GNNdR0b0wSZ48493b7j5aJGuAHGJNdRy7zwCI0ampPbb7jWykghgxDiM5Hl5.uCGGOwrdYn3x9zebNdB8b0wCmUgxA4vYYxEl4pkG0p5y8wY4Ir2V2mSWjCGKuY5s4DI4ampPjr73+pMIOtwDoZpXonMIO9b1xqgrzm9iyxialmVdLIJVdtH2.KuI69tvsyUKOdiv84bejVd2VaWrAHGJVdBwszr7bqBwwxSbEWWSTfLqzDwoJkDqsZ.yNiM7rD44reb9cL0MkemCvgicGSNWs6rR64TebtcTyMkamKvghYG0d0L6vE35K13Jw46b2zAbLPSLtIiy1tea6PpyikkeZL4UqYBbLFc.cT1TPLb2TP3tVMtqsfKkj755+Tv5+T9ERIormgShYM+1zpp30o+Do7OR1W1oKG7aT.NLuabWPImwcsXp7cohOtrxAs+iCq7REJ4p4Bqb2CwUoHPIOA5B1PJohOYTxOvBkCtuinPIseXYjMn5FLXjtX9.FIYxXjjOtLxA68KTXj5K04l7LLR50fQVsJdSZIBTRWPe.kbx7sQ8Fh9x.1tzzDDvKmluwwSQ8At26AOuBnTAew8dOWJf2FuNOqdeBF1JCvcWVY6ij6DssF9.yJG7f9gCqTbgzR4LgUtNspdeIJbRWTevDBmrsZC+Z7rKsOYWUZdRvlhFG4GJppanQjyn2EAQBSiUhlyrMUsMMEFQ3JoZYyLo0SFu7E5Fj7pHT2YMbSV9S+g8nKGZGeHrUUrub0I5xoeHHB5C9jFRVV94ED9S8O4YAzyenGxRRRycCtjrp1Jntrg7rW.gFOV.gSaiyNeHLCm1e4Qdy34IXHpwCExkKq2tbYHPvGp+vG.gC+snOayR1UjkWerjRyIQlSJhGVtbgUDYNIGHcWF2oKUzPJEZcy7DzpMPoddJdzPhGi+hGEz3wOklZHRW9S4BxUK+cwRYfZ64o3AB7n7H9nfFO9gL29j.91Wu7G7Hf.OR+AOP5hS4OuBECJ93o3ARaSJOVtSfFOdh+fPaSVZSORGV8MO10jDRWS9qIEIDeFo+5WWBwmQ5OeFoBJ93o3AhOySBZTiGAz3wS7GHFMT+0UoDhvdWP6G7Q.Q9Q5u4CKLPwGOEOPzeD9S+QnfFOdh+.Q+g5Q9CD8Gg+ZzU.owRg+ZzUfPiSThgEINdKh7XqSbKzq09Aa4fVLO+0a.WAMd7Ss.GD4yeSpgyvnXf0v1kGu2T9rX.Rsf2fVFjRSl+jgYJnwimvGH1lL+UJvfTJv7mMNCz5e3svgZgd4xOzYJDmFp+bZnnzVgPDoOtqu8nPJEzLF8mTJECWJtP18Hb0t8k8I1Borl5OYOJAArUJnQ5iaD22M1dXeRDua2WSKqNFOcoQ313uTT19R8xEG1kOGdYWzDVl90rJ2mNtv3xUOjUmtpcu3zscLdzbXa.FtsHIsLeeV2mcQ6Y9GK9e.UkGBjB-----------end_max5_patcher-----------</code></pre>

# Dependencies
Install He_Mesh and oscP5 libraries into Documents/Processing/libraries:

He_Mesh Library: http://hemesh.wblut.com/hemesh-latest.zip

oscP5 Library: http://www.sojamo.de/libraries/oscP5/download/oscP5-0.9.8.zip

A guide to installing libraries can be found here: http://www.learningprocessing.com/tutorials/libraries/