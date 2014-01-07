/**
 * PROJECT   : jPac PLCcommunication library
 * MODULE    : StringRxTx.java
 * VERSION   : -
 * DATE      : -
 * PURPOSE   : 
 * AUTHOR    : Bernd Schuster, MSK Gesellschaft fuer Automatisierung mbH, Schenefeld
 * REMARKS   : -
 * CHANGES   : CH#n <Kuerzel> <datum> <Beschreibung>
 *
 * This file is part of the jPac process automation controller.
 * jPac is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jPac is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the jPac If not, see <http://www.gnu.org/licenses/>.
 */

package org.jpac.plc;

import java.io.IOException;
import org.jpac.IndexOutOfRangeException;
import org.jpac.Value;

/**
 * Used to transfer a string value between the java application and the plc.
 * Can be used standalone or in the context of a complex data structure (STRUCT)
 */
public class StringRxTx extends LobRxTx{
    private final static int MAINTENANCEDATASIZE = 2;
    
    /**
     * @param conn an open TCP/IP connection to the plc
     * @param address address of the data item exchanged with the plc
     * @param dataOffset the byte offset inside {@link #data}, holding the data item to be exchanged with the plc
     * @param data instance of {@link Data} used to hold the data item exchanged with the plc.
     * @throws IndexOutOfRangeException
     */
    public StringRxTx(Connection conn, Address address, int dataOffset, Data data) throws IndexOutOfRangeException{
       super(conn, address, dataOffset, data);
    }

    /**
     * used to set the data item to a given value. <br>
     * The data item is written to {@link #data} first.<br>
     * The actual transfer to the plc is done by invocation of {@link #write()}: aRxTx.set(aValue).write();
     * @param value a value
     * @return this. Useful in cases where the data item should be written to the plc immediately (see above).
     * @throws AddressException
     */
    public StringRxTx set(PlcString value) throws AddressException, StringLengthException {//TODO test
        //plc strings consist of string bytes and 2 additional maintenance bytes
        int givenDataSize = value.getMaxLength() + MAINTENANCEDATASIZE;
        if (givenDataSize != getAddress().getSize())
            throw new StringLengthException("expected: " + getAddress().getSize() + " found: " + givenDataSize);
        getData().setSTRING(dataOffset, value);
        return this;
    }

    /**
     * used to set the data item to a given value. <br>
     * The data item is written to {@link #data} first.<br>
     * The actual transfer to the plc is done by invocation of {@link #write()}: aRxTx.set(aValue).write();
     * @param value a value
     * @return this. Useful in cases where the data item should be written to the plc immediately (see above).
     * @throws AddressException
     */
    public PlcString get() throws AddressException, StringLengthException {//TODO test
        return getData().getSTRING(dataOffset, address.getSize());
    }

    /**
     * used to read the data item from plc and store it inside {@link #data}.
     * @return this
     * @throws IOException
     */
    @Override
    public StringRxTx read() throws IOException{
        //make read() return a StringRxTx
        super.read();
        return this;
    }

    @Override
    public Value clone() throws CloneNotSupportedException {
        StringRxTx stringRxTx = null;
        try{stringRxTx = new StringRxTx(null, (Address)address.clone(), dataOffset, data.clone());}catch(IndexOutOfRangeException exc){/*cannot happen*/};
        return stringRxTx;
    }
}
