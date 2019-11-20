public class Triplet<A,B,C> {
    private A a;
    private B b;
    private C c;
    public Triplet(A myA,B myB,C myC)
    {
        a=myA;
        b=myB;
        c=myC;
    }
    public A first(){return a;}
    public B second(){return b;}
    public C third(){return c;}
    public String toString(){ return " | "+a+"  "+b+"  "+c+" |";}
}
