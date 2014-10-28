package com.sbstudio.seepwd.entity;

import java.util.List;

/**
 * @author water3
 * 我的区块链实体 以 聪为基本单位
 */
public class MyBlockChain {
    /** Hash 160 **/
    private String hash160;
    /** 地址 **/
    private String address;
    /** 交易次数 **/
    private int n_tx;
    /** 总接收数量 **/
    private long total_received;
    /** 总发送数量 **/
    private long total_sent;
    /** 余额 **/
    private long final_balance;
    /** 所有交易 **/
    private List<Transaction> txs;
    
    class Transaction{
        /**  **/
        private long result;
        private int block_height;
        /** 时间0时区 **/
        private long time;
        /** 输入 **/
        private List<Output> inputs;
        /** 输出个数 **/
        private int vout_sz;
        /** 交易的hash **/
        private String hash;
        /** 输入个数 **/
        private int vin_sz;
        /** 交易的Index **/
        private long tx_index;
        /** 版本？ **/
        private float ver;
        /** 输出 **/
        private List<Output> out;
        /** 大小 (字节) **/
        private int size;
        
        class Output{
            /**  **/
            private int n;
            /** 钱 **/
            private long value;
            /** 地址 **/
            private String addr;
            /** 交易的Index **/
            private long tx_index;
            /**  **/
            private int type;
            public int getN() {
                return n;
            }
            public void setN(int n) {
                this.n = n;
            }
            public long getValue() {
                return value;
            }
            public void setValue(long value) {
                this.value = value;
            }
            public String getAddr() {
                return addr;
            }
            public void setAddr(String addr) {
                this.addr = addr;
            }
            public long getTx_index() {
                return tx_index;
            }
            public void setTx_index(long tx_index) {
                this.tx_index = tx_index;
            }
            public int getType() {
                return type;
            }
            public void setType(int type) {
                this.type = type;
            }
            
        }

        public long getResult() {
            return result;
        }

        public void setResult(long result) {
            this.result = result;
        }

        public int getBlock_height() {
            return block_height;
        }

        public void setBlock_height(int block_height) {
            this.block_height = block_height;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public List<Output> getInputs() {
            return inputs;
        }

        public void setInputs(List<Output> inputs) {
            this.inputs = inputs;
        }

        public int getVout_sz() {
            return vout_sz;
        }

        public void setVout_sz(int vout_sz) {
            this.vout_sz = vout_sz;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public int getVin_sz() {
            return vin_sz;
        }

        public void setVin_sz(int vin_sz) {
            this.vin_sz = vin_sz;
        }

        public long getTx_index() {
            return tx_index;
        }

        public void setTx_index(long tx_index) {
            this.tx_index = tx_index;
        }

        public float getVer() {
            return ver;
        }

        public void setVer(float ver) {
            this.ver = ver;
        }

        public List<Output> getOut() {
            return out;
        }

        public void setOut(List<Output> out) {
            this.out = out;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }

    public String getHash160() {
        return hash160;
    }

    public void setHash160(String hash160) {
        this.hash160 = hash160;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getN_tx() {
        return n_tx;
    }

    public void setN_tx(int n_tx) {
        this.n_tx = n_tx;
    }

    public long getTotal_received() {
        return total_received;
    }

    public void setTotal_received(long total_received) {
        this.total_received = total_received;
    }

    public long getTotal_sent() {
        return total_sent;
    }

    public void setTotal_sent(long total_sent) {
        this.total_sent = total_sent;
    }

    public long getFinal_balance() {
        return final_balance;
    }

    public void setFinal_balance(long final_balance) {
        this.final_balance = final_balance;
    }

    public List<Transaction> getTxs() {
        return txs;
    }

    public void setTxs(List<Transaction> txs) {
        this.txs = txs;
    }
    
}
