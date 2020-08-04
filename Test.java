@Api(tags = { "" }, description = "t")
public class PeopleController {
    
    private MappingUtil mapper;
    private MyService MyService;
    private Validator validator;
    private MyPlaceDetailsService MyPlaceDetailsService;
    
    @Autowired
    public PeopleController(MappingUtil mapper, MyService MyService, Validator validator,
            MyPlaceDetailsService MyPlaceDetailsService) {
        this.mapper = mapper;
        this.MyService = MyService;
        this.validator = validator;
        this.MyPlaceDetailsService = MyPlaceDetailsService;
    }
    
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // Need this for the validation logic to work, BindingResult.
        binder.setValidator(validator);
    }
    
    
    @RequestMapping(value = UrlConstants.URL_My_ID, method = RequestMethod.PUT, produces = {
            MediaType.APPLICATION_JSON_UTF8_VALUE })
    @ApiOperation(value = "Update My details", notes = "Returns the updated My object.")
    @ApiResponses({
            @ApiResponse(code = org.apache.http.HttpStatus.SC_OK, response = PeoplePostResponseDto.class, message = "If the record is found and updated."),
            @ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, response = ErrorResponseDto.class, message = "If the given MyId is not a properly formatted GUID or Integer."),
            @ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, response = ErrorResponseDto.class, message = "If the given MyId was not found to be associated with any known record.")
    })
    @ResponseBody
    public ResponseEntity<MyResponseDto> updateMy(
            @Valid @RequestBody PeoplePutRequestDto peoplePutRequestDto, BindingResult result,
            @PathVariable(UrlConstants.PARAM_My_ID) String MyId) {
        
        // Validate parameter
        if (MyId == null) {
            List<String> errors = new ArrayList<String>();
            errors.add(ErrorConstants.ERR_CODE_INVALID_IDENTIFIER);
            throw new BadRequestException(errors,
                    String.format(ErrorConstants.ERR_MSG_MISSING_PARAM, UrlConstants.PARAM_My_ID));
        }
        
        if (result.hasErrors()) {
            List<String> errors = new ArrayList<String>();
            for (ObjectError error : result.getAllErrors()) {
                // Message defined on the DTOs.
                errors.add(error.getDefaultMessage());
            }
            
            throw new BadRequestException(errors, ErrorConstants.ERR_MSG_VALIDATION_FAIL);
        }
        
        My My = mapper.map(peoplePutRequestDto, My.class,
                MappingConstants.PEOPLE_PUT_REQUEST_DTO);
        My responseMy = MyService.save(My, MyId);
        
        MyResponseDto responseDto = mapper.map(responseMy, MyResponseDto.class,
                MappingConstants.PEOPLE_EXPANDED_RESPONSE_DTO);
        responseDto.setMyId(MyId);
        return new ResponseEntity<MyResponseDto>(responseDto, HttpStatus.OK);
    }
