package FrontEnd;

/**
 * Holder class for : Service
 * 
 * @author OpenORB Compiler
 */
final public class ServiceHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal Service value
     */
    public FrontEnd.Service value;

    /**
     * Default constructor
     */
    public ServiceHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public ServiceHolder(FrontEnd.Service initial)
    {
        value = initial;
    }

    /**
     * Read Service from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = ServiceHelper.read(istream);
    }

    /**
     * Write Service into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        ServiceHelper.write(ostream,value);
    }

    /**
     * Return the Service TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return ServiceHelper.type();
    }

}
