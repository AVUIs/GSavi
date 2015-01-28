# GSavi
beta code

GS.avi is a gestural instrument that generates continuous spatial visualizations and music from the input of a performer. The features extracted form a performer’s gesture defines the color, position, form and orientation of a 3-dimensional Delaunay mesh. The music, composed using granular synthesis, is generated from features extracted from the mesh. The project was created using Processing and MAX/MSP. OSC is used to communicate between the two.

# Usage

# 1: To use this patch:

A. To import the patch into MAX/MSP, Copy and Paste the enclosed compressed text into the patcher window OR Copy and from the MAX/MSP menu, choose File > New From Clipboard. 

B. To map the MAX/MSP patch to your display window, unlock the MAX/MSP patch by selecting the lock icon on the bottom left of the patcher window by clicking once. The lock should appear open. This action unlocks all of the objects in the patcher window. 

C. Select the number box to the right of “/x”. Select the “i” icon (fourth from the left) in the row if icons at the bottom of the patcher window to open the 	Inspector window. 

D. Scroll to the bottom of the list of parameters to access the options in the “Value” list. 

E. To change the range of values for the WIDTH to match your display or desired active area for the Processing sketch to run, double-click the “0” next to the 	right of “Minimum”. Change the value. Then double-click the “<none>” next to “Maximum”. Change the value. Close the Inspector window. 

F. Repeat step E to change the range of values for HEIGHT by selecting the number box above the “/y” object in the patcher window. 

G. Lock the patcher by selecting the lock icon on the lower left of the patcher. The lock should appear locked.

H. Click all of the white boxes at the top left of each object cluster for the objects “/duration”, “/phase”, “/angle”, “/scaler”, “magnitude”, and “gesture” to enable the toggle switches. An “X” should appear in all of the boxes. Numbers should start to generate for each object.  

I. When the mouse is clicked and dragged in the patcher window, an “X” will appear in the toggle box for the “/mouseState” object to verify the mouse is in its down state to enable sending information to the Processing sketch. 

J. Start the processing sketch. 

K. To begin drawing the mesh, use the trackpad or mouse within the max patch.

<pre><code>----------begin_max5_patcher----------1886.3oc6b9rbaaCDF+r8SgFN8nJKvh+2a8YnGyzICsDqMyHQpgjJ0oYx6dAojo.qs.W6Pi.akKI1jRhK9wuc+V.B4ud8UI2TcedSxheewGVb0Ue85qtp+PcG3pi+9UIayte0lrl9WVRylh040IKObpx8aq12tIus+jziGcWVc117175OlWlcyl7tyQNdtp5h7x1r1hpR22xgOk1urK+Pvjjr3uNcMJJe7knc0cEk29w57UsGdKJAjpAiQHWt.XjTijP.X4BAKkXOBOkL7QVrterTcym9UMjzcruc80c+yRjTvFS2bNJ.HnveWU1VZOa+G1eTWjsIw4LME+a+YncQ9Y3SQYaxxEI2jUd62IpjlTkX.SzyfI1KASwqXQKIoDlwnz1wrflRoFKNlTrPt.EKiPk1jZH1elMofg9hPU9+XeyOL7ZyuuOFRpyJWWscgvuRZd3zSyGvGe3C7gJkoFqTRa4iT3iOJ4KgOsU2dqUb7xSnvOh8oHnvoQr9zHFH8iXxYFwpYTQXGf0UKXDBYVzDv40DdRZ.bHBr+LqKTslQJpWDom+jFYblz3vGF.oBkkOpISZLyOenjnDPJKILV.wo1bLtNUJsFTVXo7V0U9FtphxJIFFwRrUUjyeUEPDqUUFgHiSUEvKhzWNYMN.hQNUVweViBdM.TbRHAvRYxCM6Rs84JsyRhvWtPS7VWg71stBWo5GwxtQrsUelxN5mrrBc9KqDqUUFAHkLUYrRByfW74HD79ZFgBldHw.HpTfaovjyHTxt.mQ3HTwkoDvzmR4eFgR9kSUV2TJPqFHzDUYEyOgXx3jPLqkrjX6vSbn+Vl9P5leBI3uc8gX1A0vHVYqsZUDJ4TFQBwraDQiUiHWBYcKSEjC0X7aDIjuuLhXZ3DENtN1c8v52HRnt.MhFgJTqisPeAUh0IchQI8zYhxquFquBDoSUjYmonMyR+b7e3ugWeE6XZX.ez9wPmx9gqdMrehT+GGB0a+XiT5TKYKW+N6AiAmX.ZyGt4R74h4PJbdOzKmpqNoRHsdXWNFy.vRAk0Xl+b7dXug8dnJR+HtaNv3MeX5427ADQp4iKhbce7tz9Ly6K2GfQGxLdF1OWh6gmQnBk+Cme4Th0MaBmAD+UXw2hT3H4rTI2XjLa0XlNEr0a5q03U9.l2t9OBC4zHVHS4Rahyjq8FHuf7eFgHMMUeroD+FPf38kAjTPGnf691wuADUeAZ.MBUOdKTdlpHzHaph1KSw18a6Ng7gWsc7+vwDA84p4jBxXXIJPuXLtbADXfA.MgwEDsRN6z2erli7Lq2+80FoyD0k7gGqqvqwH8EMY8s4MMY2l+HM2usdeces7E+B801a76ZkL31FIe.PJdGfn5y.HdzJ4XxezRtQ8liWyQmUM2t6xZxiRAmKdbUbRlWEG7SEGtmDFZEmYVEb1.eSbJ3boyHAGwqfiDutpjmPvQCofyc++fWvolUAWyprM40QohyEOiTbdMU0yKd1kmuNJoiaWt3M.jQb53O3rwQ6JbzYiyaOtaytsrnc+53zBvkPtRttst34kbwVSt+n5mkvHoTIgv3V5wzCafZ+5K3mzqmdTpr+agPO8rWFbzSQl2zyp8M4+YaVablep0lgutbt4mFgu7SA7F96XpyH1slMv8tKHmUUwtpMahv4GRr2ukhCeIrYfZHgALoBOEqmyMXQe5RSW5xSyGwrwmi0VN6+8BpV6hOEe.eGWDwysOul2ke3Kw4ROPIhS0hENNYda8jOu0huOJYiw.CxFWzv71hDSEsskK9A2U9nuB336JmOqZsasUw1WGol9N.ZzROS8p3lyuLq6WuqIub8hMUqx1bWUSq8tC4Lnh7RQE9+PjnG3gTI53gV0uEFTdULipN0+JR1TT9++ydT+Er63ioTS095UObO7vpBtbwoqyZq9onb3IQ+gg0Fy8EcWw504ktLZcQSWNaePRdxaWXCGMfHdzrvEOTDwipChzvDODL7gFN9fHbXgS9nvHms0bCV7nwJeBT7nvDO5vEORrwSXRuTXJ+DtpOXtaEtaVRThYHbwCF7HCHejXimvHlkBL2uBGd3XviHb3ggIdBmWgDvxm.EOXZ8QFvzcB13IP5m3p0GAlVeBWmFBLlEhv03r.iYgHblEBIV9Dn3AiYw+KneUiGN13IP5GLtEzv0Zn.hrxOXbK5gXftegwsPDN2KNlxyhvMwctF68q.EOXpOyCW8YtDa7DF8LGS8YZ.0OXpOyCW27bL0m4AjOXpOyC2rK5G5vTSFjD13Yx5Oj3hO7.GOzo72UwU7DP6TRb09COtBGlAa04.EOndNAga1NLUjc+RhkOgwMkgROGtzcVjMaGTMaDrnAvjsCgqWLPhMdBDevz6LDN0LfQMCgqWU.0J8FrvgZvd6JLxYJFyKZ3LunQVyFTTKMV3JGRiLyBJlzKZ3J+PeU6c9vNRJa2tOmW2b7yrOTR1l8op59+xDr75CaTuC+Z+djJoN+yEO75U8GIqd0cEs4q51Qa8a+o60ceqfrWmuc8+YrPlfF-----------end_max5_patcher-----------</code></pre>

# Dependencies
Install He_Mesh and oscP5 libraries into Documents/Processing/libraries:

He_Mesh Library: http://hemesh.wblut.com/hemesh-latest.zip

oscP5 Library: http://www.sojamo.de/libraries/oscP5/download/oscP5-0.9.8.zip

A guide to installing libraries can be found here: http://www.learningprocessing.com/tutorials/libraries/