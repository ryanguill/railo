<cfsavecontent variable="c">R0lGODlhEQAOAKIEAKyvuSwvOf///1lfc////wAAAAAAAAAAACH5BAEAAAQALAAAAAARAA4AAAM3SDTc3mqBSesMQ4LHBx5b93yh2JCMoK4rg6asejLlEDtv0+I0J3A5k6cn9GAkgaRymcwoigxFAgA7</cfsavecontent><cfoutput><cfif getBaseTemplatePath() EQ getCurrentTemplatePath()><cfcontent type="image/gif" variable="#toBinary(c)#"><cfsetting showdebugoutput="no"><cfelse>data:image/gif;base64,#c#</cfif></cfoutput>