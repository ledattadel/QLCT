PGDMP                          y         	   qlchitieu    12.3    12.3 /    Y
           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            Z
           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            [
           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            \
           1262    24577 	   qlchitieu    DATABASE     �   CREATE DATABASE qlchitieu WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Vietnamese_Vietnam.1252' LC_CTYPE = 'Vietnamese_Vietnam.1252';
    DROP DATABASE qlchitieu;
                postgres    false            �            1255    24829    after_delete_phatsinh()    FUNCTION     �  CREATE FUNCTION public.after_delete_phatsinh() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	begin
		if old.loaiphatsinh = 't' or old.loaiphatsinh = 'n' or old.loaiphatsinh = 'l' then
			update taikhoan 
			set sodu = sodu - old.sotien
			where ma_taikhoan = old.ma_taikhoan;
		else
			update taikhoan 
			set sodu = sodu + old.sotien
			where ma_taikhoan = old.ma_taikhoan;
		end if;
		return old;
	END;
$$;
 .   DROP FUNCTION public.after_delete_phatsinh();
       public          postgres    false            �            1255    24824    after_insert_phatsinh()    FUNCTION     �  CREATE FUNCTION public.after_insert_phatsinh() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	BEGIN
		if new.loaiphatsinh = 't' or new.loaiphatsinh = 'n' or new.loaiphatsinh = 'l' then
			update taikhoan
			set sodu = sodu + new.sotien
			where ma_taikhoan = new.ma_taikhoan;
		else
			update taikhoan
			set sodu = sodu - new.sotien
			where ma_taikhoan = new.ma_taikhoan;
		end if;
		
		
	return new;
		
	END;
$$;
 .   DROP FUNCTION public.after_insert_phatsinh();
       public          postgres    false            �            1255    24831    after_update_phatsinh()    FUNCTION     �  CREATE FUNCTION public.after_update_phatsinh() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	begin
		if new.loaiphatsinh = 't' or new.loaiphatsinh = 'n' or new.loaiphatsinh = 'l' then
			update taikhoan 
			set sodu = sodu + new.sotien - old.sotien
			where ma_taikhoan = new.ma_taikhoan;
		else
			update taikhoan 
			set sodu = sodu - new.sotien + old.sotien
			where ma_taikhoan = new.ma_taikhoan;
		end if;
	
	return new;

	END;
$$;
 .   DROP FUNCTION public.after_update_phatsinh();
       public          postgres    false            �            1255    33049 X   sp_insert_nd(character varying, character varying, character varying, character varying) 	   PROCEDURE     �  CREATE PROCEDURE public.sp_insert_nd(p_tennguoidung character varying, p_matkhau character varying, p_hoten character varying, p_sodienthoai character varying)
    LANGUAGE plpgsql
    AS $$
BEGIN
	DECLARE v_ma_taikhoan INT; v_ma_nguoidung INT;

	begin
		
	insert into taikhoan(sodu) values (0) returning ma_taikhoan into v_ma_taikhoan;

	insert into thongtin_nguoidung(hoten, gioitinh, sodienthoai, ma_taikhoan) values (p_hoten, 'm', p_sodienthoai, v_ma_taikhoan) 
	returning ma_nguoidung into v_ma_nguoidung;

	insert into nguoidung(tennguoidung , matkhau , ma_nguoidung ) values (p_tennguoidung, p_matkhau, v_ma_nguoidung);

	end;
end
$$;
 �   DROP PROCEDURE public.sp_insert_nd(p_tennguoidung character varying, p_matkhau character varying, p_hoten character varying, p_sodienthoai character varying);
       public          postgres    false            �            1255    41445 �   sp_insert_vayno(date, character varying, character, numeric, text, integer, date, character varying, character varying, character varying) 	   PROCEDURE     X  CREATE PROCEDURE public.sp_insert_vayno(p_ngay date, p_noidung character varying, p_loaiphatsinh character, p_sotien numeric, p_mota text, p_ma_taikhoan integer, p_ngaytra date, p_hoten_vayno character varying, p_diachi_vayno character varying, p_sdt_vayno character varying)
    LANGUAGE plpgsql
    AS $$
BEGIN
	DECLARE v_ma_phatsinh INT; v_result INT;

		begin
			INSERT INTO phatsinh(ngay, noidung, loaiphatsinh, sotien, mota, ma_taikhoan) 
			VALUES (p_ngay, p_noidung, p_loaiphatsinh, p_sotien, p_mota, p_ma_taikhoan)
			returning ma_phatsinh into v_ma_phatsinh;
		
			INSERT INTO vayno(ngaytra, hoten_vayno, diachi_vayno, sdt_vayno, ma_phatsinh ) 
			VALUES (p_ngaytra, p_hoten_vayno, p_diachi_vayno, p_sdt_vayno, v_ma_phatsinh) returning ma_vayno into v_result;
			
			if v_result > 0 then
			commit;
			else
			rollback;
			END IF;
		end;
end
$$;
   DROP PROCEDURE public.sp_insert_vayno(p_ngay date, p_noidung character varying, p_loaiphatsinh character, p_sotien numeric, p_mota text, p_ma_taikhoan integer, p_ngaytra date, p_hoten_vayno character varying, p_diachi_vayno character varying, p_sdt_vayno character varying);
       public          postgres    false            �            1259    24578 	   nguoidung    TABLE     �   CREATE TABLE public.nguoidung (
    tennguoidung character varying(50) NOT NULL,
    matkhau text NOT NULL,
    ma_nguoidung integer NOT NULL
);
    DROP TABLE public.nguoidung;
       public         heap    postgres    false            �            1259    24604    phatsinh    TABLE     �   CREATE TABLE public.phatsinh (
    ma_phatsinh integer NOT NULL,
    ngay date NOT NULL,
    noidung character varying(100) NOT NULL,
    loaiphatsinh "char" NOT NULL,
    sotien numeric(18,2) NOT NULL,
    mota text,
    ma_taikhoan integer NOT NULL
);
    DROP TABLE public.phatsinh;
       public         heap    postgres    false            �            1259    24602    phatsinh_ma_phatsinh_seq    SEQUENCE     �   CREATE SEQUENCE public.phatsinh_ma_phatsinh_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.phatsinh_ma_phatsinh_seq;
       public          postgres    false    139            ]
           0    0    phatsinh_ma_phatsinh_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.phatsinh_ma_phatsinh_seq OWNED BY public.phatsinh.ma_phatsinh;
          public          postgres    false    138            �            1259    24596    taikhoan    TABLE     d   CREATE TABLE public.taikhoan (
    ma_taikhoan integer NOT NULL,
    sodu numeric(18,2) NOT NULL
);
    DROP TABLE public.taikhoan;
       public         heap    postgres    false            �            1259    24594    taichinh_ma_taikhoan_seq    SEQUENCE     �   CREATE SEQUENCE public.taichinh_ma_taikhoan_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.taichinh_ma_taikhoan_seq;
       public          postgres    false    137            ^
           0    0    taichinh_ma_taikhoan_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.taichinh_ma_taikhoan_seq OWNED BY public.taikhoan.ma_taikhoan;
          public          postgres    false    136            �            1259    24585    thongtin_nguoidung    TABLE     )  CREATE TABLE public.thongtin_nguoidung (
    ma_nguoidung integer NOT NULL,
    hoten character varying(100) NOT NULL,
    gioitinh "char" NOT NULL,
    sodienthoai character varying(20),
    diachi character varying(500),
    nghenghiep character varying(50),
    ma_taikhoan integer NOT NULL
);
 &   DROP TABLE public.thongtin_nguoidung;
       public         heap    postgres    false            �            1259    24583 #   thongtin_nguoidung_ma_nguoidung_seq    SEQUENCE     �   CREATE SEQUENCE public.thongtin_nguoidung_ma_nguoidung_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 :   DROP SEQUENCE public.thongtin_nguoidung_ma_nguoidung_seq;
       public          postgres    false    135            _
           0    0 #   thongtin_nguoidung_ma_nguoidung_seq    SEQUENCE OWNED BY     k   ALTER SEQUENCE public.thongtin_nguoidung_ma_nguoidung_seq OWNED BY public.thongtin_nguoidung.ma_nguoidung;
          public          postgres    false    134            �            1259    33052    vayno    TABLE       CREATE TABLE public.vayno (
    ma_vayno integer NOT NULL,
    ngaytra date NOT NULL,
    datra integer,
    hoten_vayno character varying(100) NOT NULL,
    diachi_vayno character varying(500),
    sdt_vayno character varying(20),
    ma_phatsinh integer NOT NULL
);
    DROP TABLE public.vayno;
       public         heap    postgres    false            �            1259    33050    vayno_ma_vayno_seq    SEQUENCE     �   CREATE SEQUENCE public.vayno_ma_vayno_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.vayno_ma_vayno_seq;
       public          postgres    false    141            `
           0    0    vayno_ma_vayno_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.vayno_ma_vayno_seq OWNED BY public.vayno.ma_vayno;
          public          postgres    false    140            �	           2604    41249    phatsinh ma_phatsinh    DEFAULT     |   ALTER TABLE ONLY public.phatsinh ALTER COLUMN ma_phatsinh SET DEFAULT nextval('public.phatsinh_ma_phatsinh_seq'::regclass);
 C   ALTER TABLE public.phatsinh ALTER COLUMN ma_phatsinh DROP DEFAULT;
       public          postgres    false    138    139    139            �	           2604    41250    taikhoan ma_taikhoan    DEFAULT     |   ALTER TABLE ONLY public.taikhoan ALTER COLUMN ma_taikhoan SET DEFAULT nextval('public.taichinh_ma_taikhoan_seq'::regclass);
 C   ALTER TABLE public.taikhoan ALTER COLUMN ma_taikhoan DROP DEFAULT;
       public          postgres    false    136    137    137            �	           2604    41251    thongtin_nguoidung ma_nguoidung    DEFAULT     �   ALTER TABLE ONLY public.thongtin_nguoidung ALTER COLUMN ma_nguoidung SET DEFAULT nextval('public.thongtin_nguoidung_ma_nguoidung_seq'::regclass);
 N   ALTER TABLE public.thongtin_nguoidung ALTER COLUMN ma_nguoidung DROP DEFAULT;
       public          postgres    false    134    135    135            �	           2604    41252    vayno ma_vayno    DEFAULT     p   ALTER TABLE ONLY public.vayno ALTER COLUMN ma_vayno SET DEFAULT nextval('public.vayno_ma_vayno_seq'::regclass);
 =   ALTER TABLE public.vayno ALTER COLUMN ma_vayno DROP DEFAULT;
       public          postgres    false    141    140    141            N
          0    24578 	   nguoidung 
   TABLE DATA           H   COPY public.nguoidung (tennguoidung, matkhau, ma_nguoidung) FROM stdin;
    public          postgres    false    133   �C       T
          0    24604    phatsinh 
   TABLE DATA           g   COPY public.phatsinh (ma_phatsinh, ngay, noidung, loaiphatsinh, sotien, mota, ma_taikhoan) FROM stdin;
    public          postgres    false    139   ]G       R
          0    24596    taikhoan 
   TABLE DATA           5   COPY public.taikhoan (ma_taikhoan, sodu) FROM stdin;
    public          postgres    false    137   nL       P
          0    24585    thongtin_nguoidung 
   TABLE DATA           y   COPY public.thongtin_nguoidung (ma_nguoidung, hoten, gioitinh, sodienthoai, diachi, nghenghiep, ma_taikhoan) FROM stdin;
    public          postgres    false    135   �L       V
          0    33052    vayno 
   TABLE DATA           l   COPY public.vayno (ma_vayno, ngaytra, datra, hoten_vayno, diachi_vayno, sdt_vayno, ma_phatsinh) FROM stdin;
    public          postgres    false    141   N       a
           0    0    phatsinh_ma_phatsinh_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.phatsinh_ma_phatsinh_seq', 205, true);
          public          postgres    false    138            b
           0    0    taichinh_ma_taikhoan_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.taichinh_ma_taikhoan_seq', 18, true);
          public          postgres    false    136            c
           0    0 #   thongtin_nguoidung_ma_nguoidung_seq    SEQUENCE SET     R   SELECT pg_catalog.setval('public.thongtin_nguoidung_ma_nguoidung_seq', 17, true);
          public          postgres    false    134            d
           0    0    vayno_ma_vayno_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.vayno_ma_vayno_seq', 50, true);
          public          postgres    false    140            �	           2606    24673    nguoidung nguoidung_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.nguoidung
    ADD CONSTRAINT nguoidung_pkey PRIMARY KEY (tennguoidung);
 B   ALTER TABLE ONLY public.nguoidung DROP CONSTRAINT nguoidung_pkey;
       public            postgres    false    133            
           2606    24612    phatsinh phatsinh_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.phatsinh
    ADD CONSTRAINT phatsinh_pkey PRIMARY KEY (ma_phatsinh);
 @   ALTER TABLE ONLY public.phatsinh DROP CONSTRAINT phatsinh_pkey;
       public            postgres    false    139            
           2606    24601    taikhoan taichinh_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.taikhoan
    ADD CONSTRAINT taichinh_pkey PRIMARY KEY (ma_taikhoan);
 @   ALTER TABLE ONLY public.taikhoan DROP CONSTRAINT taichinh_pkey;
       public            postgres    false    137            �	           2606    24593 *   thongtin_nguoidung thongtin_nguoidung_pkey 
   CONSTRAINT     r   ALTER TABLE ONLY public.thongtin_nguoidung
    ADD CONSTRAINT thongtin_nguoidung_pkey PRIMARY KEY (ma_nguoidung);
 T   ALTER TABLE ONLY public.thongtin_nguoidung DROP CONSTRAINT thongtin_nguoidung_pkey;
       public            postgres    false    135            
           2606    33060    vayno vayno_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.vayno
    ADD CONSTRAINT vayno_pkey PRIMARY KEY (ma_vayno);
 :   ALTER TABLE ONLY public.vayno DROP CONSTRAINT vayno_pkey;
       public            postgres    false    141            

           2620    24881    phatsinh tr_delete_phatsinh    TRIGGER     �   CREATE TRIGGER tr_delete_phatsinh AFTER DELETE ON public.phatsinh FOR EACH ROW EXECUTE FUNCTION public.after_delete_phatsinh();
 4   DROP TRIGGER tr_delete_phatsinh ON public.phatsinh;
       public          postgres    false    143    139            
           2620    24882    phatsinh tr_insert_phatsinh    TRIGGER     �   CREATE TRIGGER tr_insert_phatsinh AFTER INSERT ON public.phatsinh FOR EACH ROW EXECUTE FUNCTION public.after_insert_phatsinh();
 4   DROP TRIGGER tr_insert_phatsinh ON public.phatsinh;
       public          postgres    false    139    142            
           2620    24883    phatsinh tr_update_phatsinh    TRIGGER     �   CREATE TRIGGER tr_update_phatsinh AFTER UPDATE ON public.phatsinh FOR EACH ROW WHEN ((old.sotien IS DISTINCT FROM new.sotien)) EXECUTE FUNCTION public.after_update_phatsinh();
 4   DROP TRIGGER tr_update_phatsinh ON public.phatsinh;
       public          postgres    false    139    139    144            
           2606    24635 %   nguoidung nguoidung_ma_nguoidung_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.nguoidung
    ADD CONSTRAINT nguoidung_ma_nguoidung_fkey FOREIGN KEY (ma_nguoidung) REFERENCES public.thongtin_nguoidung(ma_nguoidung) NOT VALID;
 O   ALTER TABLE ONLY public.nguoidung DROP CONSTRAINT nguoidung_ma_nguoidung_fkey;
       public          postgres    false    135    133    2559            
           2606    24645 "   phatsinh phatsinh_ma_taikhoan_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.phatsinh
    ADD CONSTRAINT phatsinh_ma_taikhoan_fkey FOREIGN KEY (ma_taikhoan) REFERENCES public.taikhoan(ma_taikhoan) NOT VALID;
 L   ALTER TABLE ONLY public.phatsinh DROP CONSTRAINT phatsinh_ma_taikhoan_fkey;
       public          postgres    false    2561    137    139            
           2606    24650 6   thongtin_nguoidung thongtin_nguoidung_ma_taikhoan_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.thongtin_nguoidung
    ADD CONSTRAINT thongtin_nguoidung_ma_taikhoan_fkey FOREIGN KEY (ma_taikhoan) REFERENCES public.taikhoan(ma_taikhoan) NOT VALID;
 `   ALTER TABLE ONLY public.thongtin_nguoidung DROP CONSTRAINT thongtin_nguoidung_ma_taikhoan_fkey;
       public          postgres    false    135    137    2561            	
           2606    33061    vayno vayno_ma_phatsinh_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.vayno
    ADD CONSTRAINT vayno_ma_phatsinh_fkey FOREIGN KEY (ma_phatsinh) REFERENCES public.phatsinh(ma_phatsinh);
 F   ALTER TABLE ONLY public.vayno DROP CONSTRAINT vayno_ma_phatsinh_fkey;
       public          postgres    false    141    2563    139            N
   e  x�m�ɲ�H�5>�]�ɐ���ȋ"XDm P�D���QFE�|y��O�:�D៬���L�����/��r ���ؙ���nv�ͫGM���]�O"��)Z�==��/��f,Ҿ��w0s�̧�ޭ;�OK��f�OI|�����=4̻�/N�����A���ŧ����D�lD���)b��0K���h�s+,��֡WO��D�y7�[�-]r,�r6��?OxF������nt`�e쌧̭I���^���P�,:W
��E*�� �ႁ�m�����0�0_� �_�б�᥶�Z�Wѵ)8'��(Z��"r
p��=5�,��G���Q����5�I�2��4�ݹP��B��֝���{E����͙���m�8 �M>,�8l�)�)��__�z�����UN�hŤk�/�\c�U8݂*"���}n��L�}��W�1�#e��ⱇ��:E�vN̩�Y�����#^Il�7Ǐ��C>/.�� �B��+����\��랕�9�J �5�� �9?l�.m:���ܕN�M�P�� Ҿ�^�3�0�i:�69�2�'P�1" �2?t�<����>��mø/4+��N����=���<8<��H��#M �e�6rUR|�T�~6�u��{���T��RVޮ�����0o����_&�P�����]n��vA\h� ��uͳ{���.a�Bg�����';|�ܻ��U����޽���.G0�J��2��3��r�|U*�մ��Ov�e��f�\�}�7��vNna�r�S�N�˽��V�{Ʊo���16�,�����N6����U��%�Eǡ&@�Iɜ�aXo϶��z��kڌnJ2��7��Mn6��¹      T
     x��X�n�F]S_1?�`f�r)yM���U74�B$*�� ^�E�EQ�^t�Fa��QEQ@\tAU��?�<D�I��,�{��ܹ"5(�x��!u����?l�}#0��1~��a��L;
��c|4�[����jq�%?F�l��%1�؂�߄�lu�(K�0��7(�dɧY���t�a���^
.�\&ރ�U���jlgూ���)�9K�F�'Q�ַY�l��,h|p8'"��4^/|t�������
*��K�e�*���y����8�\�����k��q�v�B/�H����{ņ��Iw$t� Å��BO�OQ�%_��$�џ>�8�M`h�-�r���M��W:b����s���-����jյKpx9�����Q��T�y�h7�@طY��Y���OQ��C�M���V�{�t����P�k� z��0��=����3q�(���,̃�r&\�}Ə�e��Y7��/�Fh΋���2vҔ*����_dɝ�����B�^�QX�кv��w�ڿ�H1��2Q���KU�bC�5)���A,nĠ�[ L�B�>�%�=h]p�:�`,��	��L�'zSHh��E��wa�
Д�dA�N���0�̓�Y]d�@o�]���\a@��>�r����"[=D(]΅!VK�{I�����H����$<oe�����=��r�]�\��D���#�Ѕ6��υ^�Ok�}n�v2I���� V���-��YE��+��l����σ��7W���t�߉'O��$Ԅ0dZ^_z�'���ܔ�IC�@Uരw(�<{�����.�cB�>O����1��;�d�\q4�zu�;T>H�<$O��S- �� �}� ]�>mhC���u��j
��j��aZ����PV��5���6�֟|��A|�ު���ә��j�Į����Y�K ��?�^Qc"{��q�j�a�{mZ"tF�����8}Tt��oINUl�^�=Ia��E�O��w�B�z�7��J�0��H�0m, �)]ܡ?G����Kj��fA�0����;��y��O��@�J�"��]9���-����*h���<P���ǭ���	W��]l�+�#l���۵��xV�J���֍��B�{x���zG4H_�+وk��M�C�\��Mj]��>�C��?Z�c��m(.=5��o[��h��\���c%���e�\�o^�̃�]�5�카X���a�|���%����w���n?�HL,-h����^#CL������p�[hm����˩�^��h      R
   T   x�E�� !�3c�^��:��\^`� *�4V��d$3�d%�e/�֊�������PB�<�L��j��v5f���      P
      x�}�Mj�0�ךS�L�Xֲ�"�Ĵ4d����""�8�u(=@oQ��{ߤ�kj�4BH��1��q&��*�-��Oٖm�~�&�a�9�	��y+P��=�a�
|��"�l��*>�Sn�\���������W��}�r����W��/I�A~�Ļ(a����nh�$�n�v��FJ)3���U�p�`��;ܥ���缺�-WO3���iy�efS�R����2Ȼ�c��c�8��U�H��[�'�f55b��q;�n��t���D63 �%��      V
   )  x���;n�@��S��`g�]Z)��Q%�rC+��D\A�49�;CR��J:�M2�V$��
�����矡(H��0�(��b|�|�7�J9}��/!Z�1��&�z�C^d΀6'Ý'�)�v��n��]gA�]4
	��Y�C�����E�����z.�=W�$��^{�?�A@��
����\�4*Gg�J���k�̹�c��=���ܡ�1�~�w�rν՛�LD/�R~��N��O�@� �E�?(:z�X*c�Q�
��.��o<uV�ԫG@��i*[�Fos$�@�C�Xr�|`�r��n̳R<+l�}0�ˢL( j���2#Z u2���ˠ����ܓl(�
�>�5�ܩ�F���z�!y��!k�>����A�fFw�u~=������my!*1���"h��ћR~a;���i��/es�؝\�Q���h�l=��_׭-#+:!'���SӔ1����N����C�X�2LJ�ͳ��CNi������|o������׿JY��l�&�Jx�Oi1,S�������g�50sd5�� �G��     