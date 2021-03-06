public interface IntSet {
	//@ public model non_null int[] ia;
    //@ public invariant ia != null && (\forall int i, j; 0 <= i && i < j && j < ia.length; ia[i] != ia[j]);

    /*@ assignable ia;
      @ ensures (\exists int i; 0 <= i && i < ia.length; ia[i] == x);
      @ ensures (\forall int i; 0 <= i && i < \old(ia.length);
                 (\exists int j; 0 <= j && j < ia.length; ia[j] == \old(ia[i])));
      @ ensures (\forall int i; 0 <= i && i < ia.length && ia[i] != x;
                 (\exists int j; 0 <= j && j < \old(ia.length); \old(ia[j]) == ia[i]));
      @*/
    public void insert(int x);

    /*@ assignable ia;
      @ ensures (\forall int i; 0 <= i && i < ia.length; ia[i] != x);
      @ ensures (\forall int i; 0 <= i && i < ia.length;
                 (\exists int j; 0 <= j < \old(ia.length); \old(ia[j]) == ia[i]));
      @ ensures (\forall int i; 0 <= i && i < \old(ia.length) && \old(ia[i]) != x;
                 (\exists int j; 0 <= j && j < ia.length; ia[j] == \old(ia[i])));
      @*/
    public void delete(int x);

    //@ ensures \result == (\exists int i; 0 <= i && i < ia.length; ia[i] == x);
    public /*@ pure @*/ boolean isIn(int x);

	//@ ensures \result == ia.length;
    public /*@ pure @*/ int size();

    /* 该方法完成两个IntSet对象所包含元素的交换, 例如：
       IntSet对象a中的元素为{1，2，3}，IntSet对象b中的元素为{4，5，6}
       经过交换操作后，a中的元素应为{4, 5, 6}, b中的元素为{1, 2, 3}
       两个IntSet对象中元素的数量可以不相同，例如：
       IntSet对象a中的元素为{1，2，3，4}，IntSet对象b中的元素为{5，6}
       经过交换操作后，a中的元素应为{5, 6}, b中的元素为{1, 2, 3，4}
       该方法无返回值
      */
    /*
      @ public normal_behavior
      @ assignable ia;
      @ ensures (\forall int i;0 <= i && i < \old(a.length);ia[i] = \old(a[i]));
      @ ensures (\forall int i;\old(a.length) <= i && i < \old(ia.length);ia[i] = 0));
      @ ensures (\forall int i;0 <= i && i < \old(ia.length);a[i] = \old(ia[i]));
      @ ensures (\forall int i;\old(ia.length) <= i && i < \old(a.length);a[i] = 0));
     */
    public void elementSwap(IntSet a);

    /* 该方法返回两个IntSet对象的对称差运算结果
       数学上，两个集合的对称差是只属于其中一个集合，而不属于另一个集合的元素组成的集合
       集合论中的这个运算相当于布尔逻辑中的异或运算，如 A,B两个集合的对称差记为A⊕B，
       则 A⊕B = (A-B)∪(B-A) = (A∪B)-(A∩B)
       例如：集合{1,2,3}和{3,4}的对称差为{1,2,4}
       如果对空的IntSet对象进行对称差的运算，将抛出NullPointerException的异常
      */
    /*
      @ public normal_behavior
      @ requires (a != null && ia != null);
      @ assignable \nothing;
      @ ensures (\forall int i;0 <= i && i < \result.length;
      ((\exist int j;0 <= j && j <= a.length; a[j] == \result[i]) || (\exist int j;0 <= j && j <= ia.length; ia[j] == \result[i]))
      && !((\exist int j;0 <= j && j <= a.length; a[j] == \result[i]) && (\exist int j;0 <= j && j <= ia.length; ia[j] == \result[i])));
      @ also
      @ public exceptional_behavior
      @ signals (NullPointerException e) a == null || ia == null;
     */
    public /*@ pure @*/ IntSet symmetricDifference(IntSet a) throws NullPointerException;

    //@ ensures \result == (ia != null) && (\forall int i, j ; 0 <= i && i < j && j < ia.length; ia[i] != ia[j]);
    public /*@ pure @*/ boolean repOK();
}
