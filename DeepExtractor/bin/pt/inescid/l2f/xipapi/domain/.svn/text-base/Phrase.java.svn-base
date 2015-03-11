package pt.inescid.l2f.xipapi.domain;

@Deprecated    //   <---------------------------------- is used or not ??????????
public class Phrase extends XIPNode {      //<--------- sentence ?????????

//##############################################################################
//   ###########################################################################
//      ATTRIBUTES
//   ###########################################################################
//##############################################################################
   
   /** The phrase node-number */
	private String nodeNumber;

   /** The document where this phrase belogs to */
	private XipDocument parentDocument; 


//##############################################################################
//   ###########################################################################
//      CONSTUCTORS
//   ###########################################################################
//##############################################################################
   
  /**
   * Constructor.
   * 
   * @param parentNode     ?????????
   * @param name           ?????????
   * @param start          ?????????
   * @param end            ?????????
   * @param nodeNumber     ?????????
   * @param sentenceNumber ?????????
   */
	public Phrase(XIPNode parentNode, String name, String start, String end,
			 String nodeNumber, int sentenceNumber, int nodeNum) {
		super(parentNode, name, start, end, nodeNumber, sentenceNumber, nodeNum);
	}


//##############################################################################
//   ###########################################################################
//      GETTERS AND SETTERS
//   ###########################################################################
//##############################################################################
   
	public String getNodeNumber() {
		return nodeNumber;
	}

	public void setNodeNumber(String nodeNumber) {
		this.nodeNumber = nodeNumber;
	}

	public void setParentDocument(XipDocument parent) {
		this.parentDocument = parent;
	}

	public XipDocument getParentDocument() {
		return parentDocument;
	}

}
