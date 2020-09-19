package cn.csl.basics.model;

import java.util.ArrayList;
import java.util.List;

public class CxSelect {
    private String v;
    private String n;
    private String p;
    private List<CxSelect> s;
    public CxSelect(String v,String n,String p){
        this.v = v;
        this.n = n;
        this.p = p;
    }
    public List<CxSelect> getS() {
        return s;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getN() {
        return n;
    }

    public void setS(List<CxSelect> s) {
        this.s = s;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    /**
     * 递归查找子节点
     * @param treeNodes
     * @return
     */
    public static CxSelect findChildren(CxSelect treeNode,List<CxSelect> treeNodes) {
        for (CxSelect it : treeNodes) {
            if(treeNode.getV().equals(it.getP())) {
                if (treeNode.getS() == null) {
                    treeNode.setS(new ArrayList<CxSelect>());
                }
                treeNode.getS().add(findChildren(it,treeNodes));
            }
        }
        return treeNode;
    }

    /**
     * 使用递归方法建树
     * @return
     */
    public static List<CxSelect> buildByRecursive(List<CxSelect> treeNodes) {
        List<CxSelect> trees = new ArrayList<CxSelect>();
        for (CxSelect treeNode : treeNodes) {
            if ("0".equals(treeNode.getP())) {
                trees.add(findChildren(treeNode,treeNodes));
            }
        }
        return trees;
    }
}
