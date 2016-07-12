package FrontEnd;

/** 
 * Helper class for : Service
 *  
 * @author OpenORB Compiler
 */ 
public class ServiceHelper
{
    /**
     * Insert Service into an any
     * @param a an any
     * @param t Service value
     */
    public static void insert(org.omg.CORBA.Any a, FrontEnd.Service t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract Service from an any
     *
     * @param a an any
     * @return the extracted Service value
     */
    public static FrontEnd.Service extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        try
        {
            return FrontEnd.ServiceHelper.narrow( a.extract_Object() );
        }
        catch ( final org.omg.CORBA.BAD_PARAM e )
        {
            throw new org.omg.CORBA.MARSHAL(e.getMessage());
        }
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the Service TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc( id(), "Service" );
        }
        return _tc;
    }

    /**
     * Return the Service IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:FrontEnd/Service:1.0";

    /**
     * Read Service from a marshalled stream
     * @param istream the input stream
     * @return the readed Service value
     */
    public static FrontEnd.Service read(org.omg.CORBA.portable.InputStream istream)
    {
        return(FrontEnd.Service)istream.read_Object(FrontEnd._ServiceStub.class);
    }

    /**
     * Write Service into a marshalled stream
     * @param ostream the output stream
     * @param value Service value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, FrontEnd.Service value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to Service
     * @param obj the CORBA Object
     * @return Service Object
     */
    public static Service narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof Service)
            return (Service)obj;

        if (obj._is_a(id()))
        {
            _ServiceStub stub = new _ServiceStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to Service
     * @param obj the CORBA Object
     * @return Service Object
     */
    public static Service unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof Service)
            return (Service)obj;

        _ServiceStub stub = new _ServiceStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
